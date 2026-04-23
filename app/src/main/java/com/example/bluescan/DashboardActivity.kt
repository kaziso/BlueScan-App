package com.example.bluescan

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardActivity : AppCompatActivity() {

    private lateinit var repo: CsvRepository
    private lateinit var projectUri: Uri

    private lateinit var spinnerTables: Spinner
    private lateinit var contentContainer: LinearLayout
    private var activeTableIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val uriString = intent.getStringExtra("PROJECT_URI") ?: run {
            finish()
            return
        }

        projectUri = Uri.parse(uriString)
        repo = CsvRepository(this)

        spinnerTables = findViewById(R.id.spinnerTables)
        contentContainer = findViewById(R.id.llActiveTableContainer)

        findViewById<View>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<View>(R.id.btnAddTable).setOnClickListener {
            val newTable = TableData(
                tableName = repo.tables.lastOrNull()?.tableName ?: "Project",
                date = "New Date",
                startUnit = 1,
                endUnit = 200
            )
            repo.addTable(projectUri, newTable)
            refreshDashboard()
        }

        findViewById<FloatingActionButton>(R.id.fabScan).setOnClickListener {
            if (repo.tables.isNotEmpty()) {
                val intent = Intent(this, ScanActivity::class.java)
                intent.putExtra("PROJECT_URI", projectUri.toString())
                intent.putExtra("TABLE_INDEX", activeTableIndex)
                startActivity(intent)
            }
        }

        spinnerTables.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                activeTableIndex = position
                renderTable(activeTableIndex)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onResume() {
        super.onResume()
        refreshDashboard()
    }

    /** 🔥 METHOD RESMI UNTUK DIPANGGIL DARI FRAGMENT */
    fun refreshDashboard() {
        repo.init(projectUri)

        val tableNames = repo.tables.map {
            "${it.date} (${it.startUnit}-${it.endUnit})"
        }

        val adapter = ArrayAdapter(this, R.layout.spinner_item_premium, tableNames)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_premium)
        spinnerTables.adapter = adapter

        if (repo.tables.isNotEmpty()) {
            if (activeTableIndex >= repo.tables.size) {
                activeTableIndex = repo.tables.lastIndex
            }
            spinnerTables.setSelection(activeTableIndex)
            renderTable(activeTableIndex)
        }
    }

    private fun renderTable(index: Int) {
        if (index !in repo.tables.indices) return
        val table = repo.tables[index]

        contentContainer.removeAllViews()
        val view = LayoutInflater.from(this)
            .inflate(R.layout.layout_active_table, contentContainer, true)

        view.findViewById<TextView>(R.id.tvProjectInfoName).text = table.tableName
        view.findViewById<TextView>(R.id.tvProjectInfoDate).text = table.date
        view.findViewById<TextView>(R.id.tvProjectInfoRange).text =
            "(${table.startUnit}-${table.endUnit})" // Format (1-200)

        view.findViewById<View>(R.id.btnEdit).setOnClickListener {
            val bottomSheet = EditProjectBottomSheetFragment()
            bottomSheet.arguments = Bundle().apply {
                putString("PROJECT_URI", projectUri.toString())
                putInt("TABLE_INDEX", index)
            }
            bottomSheet.show(supportFragmentManager, "EditTable")
        }

        val llUnitHeaders = view.findViewById<LinearLayout>(R.id.llUnitHeaders)
        val llPartNames = view.findViewById<LinearLayout>(R.id.llPartNames)
        val llMatrixBody = view.findViewById<LinearLayout>(R.id.llMatrixBody)

        val hsvHeader = view.findViewById<HorizontalScrollView>(R.id.hsvHeader)
        val hsvBody = view.findViewById<HorizontalScrollView>(R.id.hsvBody)

        ScrollSync(hsvHeader, hsvBody)

        llUnitHeaders.removeAllViews()
        for (unit in table.startUnit..table.endUnit) {
            val tv = TextView(this)
            tv.text = "Unit $unit"
            tv.gravity = Gravity.CENTER
            tv.setTypeface(null, Typeface.BOLD)
            tv.width = 200
            tv.setPadding(16, 16, 16, 16)
            tv.setBackgroundColor(Color.parseColor("#E8F0FE")) // Light Blue Header
            tv.setTextColor(Color.parseColor("#3366cc")) // Blue Text for Headers
            
            val params = LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.MATCH_PARENT)
            params.setMargins(1, 0, 1, 0) // Tiny gap for grid look
            llUnitHeaders.addView(tv, params)
        }

        llPartNames.removeAllViews()
        llMatrixBody.removeAllViews()

        for (part in table.parts) {
            val tvName = TextView(this)
            tvName.text = part
            tvName.height = 120 
            tvName.gravity = Gravity.CENTER_VERTICAL or Gravity.START
            tvName.setPadding(32, 16, 16, 16)
            tvName.setTypeface(null, Typeface.BOLD)
            tvName.setTextColor(Color.BLACK)
            llPartNames.addView(tvName)

            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL

            val data = table.matrix[part]
            for (unit in table.startUnit..table.endUnit) {
                val cell = TextView(this)
                val scanned = data?.containsKey(unit) == true
                
                if (scanned) {
                    cell.text = "✓"
                    cell.setTextColor(Color.parseColor("#3366cc"))
                    cell.setTypeface(null, Typeface.BOLD)
                    cell.setBackgroundColor(Color.parseColor("#F0F5FF")) // Very subtle blue
                } else {
                    cell.text = "-"
                    cell.setTextColor(Color.BLACK) // PURE BLACK as requested
                    cell.setTypeface(null, Typeface.BOLD) // Bold as requested
                    cell.setBackgroundColor(Color.WHITE)
                }
                
                cell.gravity = Gravity.CENTER
                
                val params = LinearLayout.LayoutParams(200, 120) // Match Height
                params.setMargins(1, 1, 1, 1) // Grid lines
                row.addView(cell, params)
            }
            llMatrixBody.addView(row)
        }
    }
}
