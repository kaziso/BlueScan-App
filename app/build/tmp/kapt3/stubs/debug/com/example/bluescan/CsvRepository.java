package com.example.bluescan;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\t\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\tJ\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0006J\u000e\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fJ\u0010\u0010\u0015\u001a\u00020\t2\u0006\u0010\u0016\u001a\u00020\u0006H\u0002J\u0010\u0010\u0017\u001a\u00020\r2\u0006\u0010\u0018\u001a\u00020\u000fH\u0002J\u0010\u0010\u0019\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002J.\u0010\u001a\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u00062\u0006\u0010\u0013\u001a\u00020\u0006J\u001e\u0010\u001f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010 \u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\""}, d2 = {"Lcom/example/bluescan/CsvRepository;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "fileName", "", "tables", "", "Lcom/example/bluescan/TableData;", "getTables", "()Ljava/util/List;", "addTable", "", "folderUri", "Landroid/net/Uri;", "newTable", "checkDuplicateGlobal", "", "value", "init", "parseBlockHeader", "header", "readAll", "fileUri", "saveAll", "updateCell", "tableIndex", "", "unit", "part", "updateTable", "index", "updatedTable", "app_debug"})
public final class CsvRepository {
    @org.jetbrains.annotations.NotNull()
    private final android.content.Context context = null;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String fileName = "bluescan_data.csv";
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.example.bluescan.TableData> tables = null;
    
    public CsvRepository(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.example.bluescan.TableData> getTables() {
        return null;
    }
    
    public final void init(@org.jetbrains.annotations.NotNull()
    android.net.Uri folderUri) {
    }
    
    public final void addTable(@org.jetbrains.annotations.NotNull()
    android.net.Uri folderUri, @org.jetbrains.annotations.NotNull()
    com.example.bluescan.TableData newTable) {
    }
    
    public final void updateTable(@org.jetbrains.annotations.NotNull()
    android.net.Uri folderUri, int index, @org.jetbrains.annotations.NotNull()
    com.example.bluescan.TableData updatedTable) {
    }
    
    public final void updateCell(@org.jetbrains.annotations.NotNull()
    android.net.Uri folderUri, int tableIndex, int unit, @org.jetbrains.annotations.NotNull()
    java.lang.String part, @org.jetbrains.annotations.NotNull()
    java.lang.String value) {
    }
    
    public final boolean checkDuplicateGlobal(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return false;
    }
    
    private final void saveAll(android.net.Uri folderUri) {
    }
    
    private final void readAll(android.net.Uri fileUri) {
    }
    
    private final com.example.bluescan.TableData parseBlockHeader(java.lang.String header) {
        return null;
    }
}