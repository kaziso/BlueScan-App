package com.example.bluescan.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0082@\u00a2\u0006\u0002\u0010\rJZ\u0010\u000e\u001a\u00020\n2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\b\u0010\u0011\u001a\u0004\u0018\u00010\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u00162\u0006\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u0016H\u0086@\u00a2\u0006\u0002\u0010\u001cR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/example/bluescan/data/ScanRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "api", "Lcom/example/bluescan/data/remote/ApiService;", "dao", "Lcom/example/bluescan/data/local/ScanDao;", "saveOffline", "Lcom/example/bluescan/data/ScanResult;", "entity", "Lcom/example/bluescan/data/local/ScanEntity;", "(Lcom/example/bluescan/data/local/ScanEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "scan", "legacyRepo", "Lcom/example/bluescan/CsvRepository;", "folderUri", "Landroid/net/Uri;", "tableIndex", "", "projectId", "", "date", "technicianId", "unit", "part", "barcode", "(Lcom/example/bluescan/CsvRepository;Landroid/net/Uri;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class ScanRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.bluescan.data.local.ScanDao dao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.bluescan.data.remote.ApiService api = null;
    
    public ScanRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object scan(@org.jetbrains.annotations.Nullable()
    com.example.bluescan.CsvRepository legacyRepo, @org.jetbrains.annotations.Nullable()
    android.net.Uri folderUri, int tableIndex, @org.jetbrains.annotations.NotNull()
    java.lang.String projectId, @org.jetbrains.annotations.NotNull()
    java.lang.String date, @org.jetbrains.annotations.NotNull()
    java.lang.String technicianId, int unit, @org.jetbrains.annotations.NotNull()
    java.lang.String part, @org.jetbrains.annotations.NotNull()
    java.lang.String barcode, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.bluescan.data.ScanResult> $completion) {
        return null;
    }
    
    private final java.lang.Object saveOffline(com.example.bluescan.data.local.ScanEntity entity, kotlin.coroutines.Continuation<? super com.example.bluescan.data.ScanResult> $completion) {
        return null;
    }
}