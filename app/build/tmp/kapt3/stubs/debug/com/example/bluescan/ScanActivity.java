package com.example.bluescan;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001:\u00017B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010#\u001a\u00020$H\u0002J\u0010\u0010%\u001a\u00020$2\u0006\u0010&\u001a\u00020 H\u0002J\b\u0010\'\u001a\u00020$H\u0002J\u0010\u0010(\u001a\u00020$2\u0006\u0010)\u001a\u00020\u0013H\u0002J\u0012\u0010*\u001a\u00020$2\b\u0010+\u001a\u0004\u0018\u00010,H\u0014J\b\u0010-\u001a\u00020$H\u0014J\u0010\u0010.\u001a\u00020$2\u0006\u0010/\u001a\u000200H\u0003J\b\u00101\u001a\u00020$H\u0002J\b\u00102\u001a\u00020$H\u0002J\u0018\u00103\u001a\u00020$2\u0006\u00104\u001a\u00020\u00132\u0006\u00105\u001a\u00020\u000fH\u0002J\b\u00106\u001a\u00020$H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \u000b*\u0004\u0018\u00010\n0\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082.\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u0018\u001a\u0010\u0012\f\u0012\n \u000b*\u0004\u0018\u00010\u00130\u00130\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001dX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00068"}, d2 = {"Lcom/example/bluescan/ScanActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "COOLDOWN_MS", "", "activeTable", "Lcom/example/bluescan/TableData;", "camera", "Landroidx/camera/core/Camera;", "cameraExecutor", "Ljava/util/concurrent/ExecutorService;", "kotlin.jvm.PlatformType", "currentState", "Lcom/example/bluescan/ScanActivity$ScanState;", "isFlashOn", "", "isFrontCamera", "lastScanTimestamp", "lastScannedCode", "", "projectUri", "Landroid/net/Uri;", "repo", "Lcom/example/bluescan/CsvRepository;", "requestPermissionLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "scanRepo", "Lcom/example/bluescan/data/ScanRepository;", "spinnerPart", "Landroid/widget/Spinner;", "spinnerUnit", "tableIndex", "", "toneGenerator", "Landroid/media/ToneGenerator;", "advancePart", "", "advanceUnitTo", "nextUnit", "findAndSetFirstEmptySlot", "handleBarcode", "barcodeValue", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "processImage", "proxy", "Landroidx/camera/core/ImageProxy;", "setupControls", "setupSpinners", "showToast", "msg", "center", "startCamera", "ScanState", "app_debug"})
public final class ScanActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.bluescan.CsvRepository repo;
    private com.example.bluescan.data.ScanRepository scanRepo;
    private android.net.Uri projectUri;
    private android.widget.Spinner spinnerUnit;
    private android.widget.Spinner spinnerPart;
    private com.example.bluescan.TableData activeTable;
    private int tableIndex = -1;
    private final java.util.concurrent.ExecutorService cameraExecutor = null;
    @org.jetbrains.annotations.Nullable()
    private androidx.camera.core.Camera camera;
    private boolean isFlashOn = false;
    private boolean isFrontCamera = false;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.NotNull()
    private volatile com.example.bluescan.ScanActivity.ScanState currentState = com.example.bluescan.ScanActivity.ScanState.IDLE;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile java.lang.String lastScannedCode;
    private long lastScanTimestamp = 0L;
    private final long COOLDOWN_MS = 1500L;
    @org.jetbrains.annotations.NotNull()
    private final android.media.ToneGenerator toneGenerator = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> requestPermissionLauncher = null;
    
    public ScanActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void findAndSetFirstEmptySlot() {
    }
    
    @java.lang.Override()
    protected void onDestroy() {
    }
    
    private final void setupSpinners() {
    }
    
    private final void setupControls() {
    }
    
    private final void startCamera() {
    }
    
    @androidx.annotation.OptIn(markerClass = {androidx.camera.core.ExperimentalGetImage.class})
    private final void processImage(androidx.camera.core.ImageProxy proxy) {
    }
    
    private final void handleBarcode(java.lang.String barcodeValue) {
    }
    
    private final void showToast(java.lang.String msg, boolean center) {
    }
    
    private final void advancePart() {
    }
    
    private final void advanceUnitTo(int nextUnit) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/example/bluescan/ScanActivity$ScanState;", "", "(Ljava/lang/String;I)V", "IDLE", "PROCESSING", "COOLDOWN", "LOCKED_SUCCESS", "LOCKED_DUPLICATE_EXIT", "app_debug"})
    public static enum ScanState {
        /*public static final*/ IDLE /* = new IDLE() */,
        /*public static final*/ PROCESSING /* = new PROCESSING() */,
        /*public static final*/ COOLDOWN /* = new COOLDOWN() */,
        /*public static final*/ LOCKED_SUCCESS /* = new LOCKED_SUCCESS() */,
        /*public static final*/ LOCKED_DUPLICATE_EXIT /* = new LOCKED_DUPLICATE_EXIT() */;
        
        ScanState() {
        }
        
        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.example.bluescan.ScanActivity.ScanState> getEntries() {
            return null;
        }
    }
}