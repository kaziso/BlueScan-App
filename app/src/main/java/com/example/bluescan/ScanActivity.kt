package com.example.bluescan

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class ScanActivity : AppCompatActivity() {

    private lateinit var repo: CsvRepository
    private lateinit var scanRepo: com.example.bluescan.data.ScanRepository
    private lateinit var projectUri: Uri
    private lateinit var spinnerUnit: Spinner
    private lateinit var spinnerPart: Spinner
    private lateinit var activeTable: TableData
    private var tableIndex: Int = -1

    private val cameraExecutor = Executors.newSingleThreadExecutor()
    private var camera: Camera? = null
    private var isFlashOn = false
    private var isFrontCamera = false

    // STRICT STATE MACHINE
    enum class ScanState {
        IDLE,           // Looking for barcodes
        PROCESSING,     // Found barcode, calling Repo
        COOLDOWN,       // Global cooldown (1-2s)
        LOCKED_SUCCESS, // Saved, waiting for empty frame to reset
        LOCKED_DUPLICATE_EXIT // Duplicate found, waiting to exit
    }

    @Volatile private var currentState = ScanState.IDLE
    @Volatile private var lastScannedCode: String? = null
    private var lastScanTimestamp: Long = 0
    private val COOLDOWN_MS = 1500L

    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) startCamera() else finish()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        val uriString = intent.getStringExtra("PROJECT_URI")
        tableIndex = intent.getIntExtra("TABLE_INDEX", -1)

        if (uriString == null || tableIndex == -1) {
            finish()
            return
        }

        projectUri = Uri.parse(uriString)
        repo = CsvRepository(this)
        repo.init(projectUri)

        if (tableIndex !in repo.tables.indices) {
            finish()
            return
        }

        activeTable = repo.tables[tableIndex]
        scanRepo = com.example.bluescan.data.ScanRepository(this)

        spinnerUnit = findViewById(R.id.spinnerUnit)
        spinnerPart = findViewById(R.id.spinnerPart)

        setupSpinners()
        
        // SMART AUTO-FOCUS (Requirement #5)
        findAndSetFirstEmptySlot()
        
        setupControls()

        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun findAndSetFirstEmptySlot() {
        // Iterate Units then Parts to find first missing entry
        for (unit in activeTable.startUnit..activeTable.endUnit) {
            for (part in activeTable.parts) {
                // Check exact map structure: matrix[part][unit]
                val partMap = activeTable.matrix[part]
                // If map missing OR unit key missing -> Empty Slot
                if (partMap == null || !partMap.containsKey(unit)) {
                    // Found it!
                    val unitIndex = spinnerUnit.adapter.count.let { count ->
                        (0 until count).firstOrNull { spinnerUnit.getItemAtPosition(it).toString() == "Unit $unit" }
                    }
                    val partIndex = spinnerPart.adapter.count.let { count ->
                        (0 until count).firstOrNull { spinnerPart.getItemAtPosition(it).toString() == part }
                    }

                    if (unitIndex != null && partIndex != null) {
                        spinnerUnit.setSelection(unitIndex)
                        spinnerPart.setSelection(partIndex)
                        return // Done
                    }
                }
            }
        }
        // If all full, defaults to Unit 1 Part 1 (already set)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        toneGenerator.release()
    }

    private fun setupSpinners() {
        val units = (activeTable.startUnit..activeTable.endUnit).map { "Unit $it" }
        spinnerUnit.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, units)

        spinnerPart.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, activeTable.parts)

        // Manual Change -> Reset State (User intervention overrides locks)
        val resetListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: android.view.View?, pos: Int, id: Long) {
                 // Only reset if locked success. If processing, let it finish.
                 if (currentState == ScanState.LOCKED_SUCCESS) {
                     currentState = ScanState.IDLE
                     lastScannedCode = null
                 }
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
        spinnerUnit.onItemSelectedListener = resetListener
        spinnerPart.onItemSelectedListener = resetListener
    }

    private fun setupControls() {
        val btnFlash = findViewById<ImageButton>(R.id.btnFlash)
        val btnFlip = findViewById<ImageButton>(R.id.btnFlipCamera)
        val zoomSlider = findViewById<SeekBar>(R.id.zoomSlider)

        btnFlash.setOnClickListener {
            isFlashOn = !isFlashOn
            camera?.cameraControl?.enableTorch(isFlashOn)
        }
        btnFlip.setOnClickListener {
            isFrontCamera = !isFrontCamera
            startCamera()
        }
        zoomSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, v: Int, fromUser: Boolean) {
                camera?.cameraControl?.setLinearZoom(v / 100f)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun startCamera() {
        val providerFuture = ProcessCameraProvider.getInstance(this)
        providerFuture.addListener({
            val provider = providerFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(findViewById<PreviewView>(R.id.viewFinder).surfaceProvider)
            }
            val analysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build().also {
                    it.setAnalyzer(cameraExecutor) { proxy -> processImage(proxy) }
                }

            val selector = if (isFrontCamera) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
            provider.unbindAll()
            camera = provider.bindToLifecycle(this, selector, preview, analysis)
            camera?.cameraControl?.enableTorch(isFlashOn)
        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImage(proxy: ImageProxy) {
        val mediaImage = proxy.image ?: run { proxy.close(); return }

        // 1. STATE CHECK: Ignore if busy or exiting
        if (currentState == ScanState.PROCESSING || 
            currentState == ScanState.LOCKED_DUPLICATE_EXIT) {
            proxy.close()
            return
        }

        // 2. COOLDOWN CHECK
        if (System.currentTimeMillis() - lastScanTimestamp < COOLDOWN_MS) {
            // Still in cooldown, ignore frames
            proxy.close()
            return
        }
        // Force IDLE if we were in COOLDOWN and time passed
        if (currentState == ScanState.COOLDOWN) {
            currentState = ScanState.IDLE
        }

        val image = InputImage.fromMediaImage(mediaImage, proxy.imageInfo.rotationDegrees)
        val options = BarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_QR_CODE, Barcode.FORMAT_CODE_128, Barcode.FORMAT_DATA_MATRIX,
            Barcode.FORMAT_AZTEC, Barcode.FORMAT_EAN_13, Barcode.FORMAT_UPC_A, Barcode.FORMAT_CODE_39
        ).build()

        // ROI Calculation (Strict 20% - 80%)
        val w = mediaImage.width
        val h = mediaImage.height
        val roiRect = android.graphics.Rect((w * 0.2).toInt(), (h * 0.2).toInt(), (w * 0.8).toInt(), (h * 0.8).toInt())

        BarcodeScanning.getClient(options).process(image)
            .addOnSuccessListener { barcodes ->
                // 3. STRICT ROI FILTER
                val validBarcodes = barcodes.filter { b ->
                    val box = b.boundingBox ?: return@filter false
                    // STRICT: Bounding box must be FULLY contained in ROI
                    roiRect.contains(box)
                }

                // 4. EMPTY FRAME LOGIC
                if (validBarcodes.isEmpty()) {
                    // Unlock only if we were waiting for empty frame
                    if (currentState == ScanState.LOCKED_SUCCESS) {
                        currentState = ScanState.IDLE
                        lastScannedCode = null
                    }
                    return@addOnSuccessListener
                }

                // We have a VALID, CENTERED barcode
                val code = validBarcodes[0].rawValue ?: return@addOnSuccessListener

                // 5. CHECK LOCKS & REPEATS
                if (currentState == ScanState.LOCKED_SUCCESS) {
                    // Ignore everything until frame clears
                    return@addOnSuccessListener
                }
                if (code == lastScannedCode) {
                    // Ignore repeat
                    return@addOnSuccessListener
                }

                // 6. START PROCESSING
                if (currentState == ScanState.IDLE) {
                    handleBarcode(code)
                }
            }
            .addOnFailureListener { }
            .addOnCompleteListener { proxy.close() }
    }

    private fun handleBarcode(barcodeValue: String) {
        currentState = ScanState.PROCESSING
        lastScannedCode = barcodeValue
        lastScanTimestamp = System.currentTimeMillis()

        val unit = spinnerUnit.selectedItem.toString().replace("Unit ", "").toIntOrNull() ?: 1
        val part = spinnerPart.selectedItem.toString()

        lifecycleScope.launch {
            try {
                // Call Repository
                val result = scanRepo.scan(
                    repo, // Legacy CSV Support
                    projectUri,
                    tableIndex,
                    activeTable.tableName,
                    activeTable.date,
                    "T01",
                    unit,
                    part,
                    barcodeValue
                )

                when (result) {
                    is com.example.bluescan.data.ScanResult.Success -> {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                        showToast("Saved: $part", false)
                        advancePart()
                        // Enter Cooldown -> Then Lock
                        currentState = ScanState.COOLDOWN
                        // After cooldown, it will become LOCKED_SUCCESS (via logic adjustment or re-flagging)
                        // Actually, let's just go to LOCKED_SUCCESS but rely on timestamp for input block.
                        // Better: Set LOCKED_SUCCESS now, timestamp handles the "silence".
                        currentState = ScanState.LOCKED_SUCCESS
                    }
                    is com.example.bluescan.data.ScanResult.UnitCompleted -> {
                        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 300)
                        showToast("Unit $unit Completed!", false)
                        advanceUnitTo(result.nextUnit)
                        currentState = ScanState.LOCKED_SUCCESS
                    }
                    is com.example.bluescan.data.ScanResult.Duplicate -> {
                        currentState = ScanState.LOCKED_DUPLICATE_EXIT
                        toneGenerator.startTone(ToneGenerator.TONE_SUP_ERROR, 500)
                        showToast("DUPLICATE: ${result.message}", true)
                        kotlinx.coroutines.delay(1200)
                        finish()
                    }
                    is com.example.bluescan.data.ScanResult.OfflineQueued -> {
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 150)
                        showToast("Saved (Offline)", false)
                        advancePart()
                        currentState = ScanState.LOCKED_SUCCESS
                    }
                    is com.example.bluescan.data.ScanResult.Error -> {
                         showToast("Error: ${result.message}", false)
                         currentState = ScanState.IDLE
                         lastScannedCode = null
                    }
                }
            } catch (e: Exception) {
                showToast("System Error: ${e.message}", false)
                currentState = ScanState.IDLE
            }
        }
    }
    
    private fun showToast(msg: String, center: Boolean) {
        val t = Toast.makeText(this@ScanActivity, msg, Toast.LENGTH_SHORT)
        if (center) t.setGravity(android.view.Gravity.CENTER, 0, 0)
        else t.setGravity(android.view.Gravity.TOP or android.view.Gravity.CENTER_HORIZONTAL, 0, 150)
        t.show()
    }

    private fun advancePart() {
        val partPos = spinnerPart.selectedItemPosition
        val unitPos = spinnerUnit.selectedItemPosition
        if (partPos < spinnerPart.adapter.count - 1) {
            spinnerPart.setSelection(partPos + 1)
        } else if (unitPos < spinnerUnit.adapter.count - 1) {
            spinnerUnit.setSelection(unitPos + 1)
            spinnerPart.setSelection(0)
        }
    }

    private fun advanceUnitTo(nextUnit: Int) {
        val adapter = spinnerUnit.adapter as ArrayAdapter<String>
        val text = "Unit $nextUnit"
        val pos = adapter.getPosition(text)
        if (pos >= 0) {
            spinnerUnit.setSelection(pos)
            spinnerPart.setSelection(0)
        }
    }
}
