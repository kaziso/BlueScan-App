package com.example.bluescan.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScanDao {
    @Insert
    suspend fun insertScan(scan: ScanEntity): Long

    @Query("SELECT * FROM scans WHERE isSynced = 0")
    suspend fun getUnsyncedScans(): List<ScanEntity>

    @Update
    suspend fun updateScan(scan: ScanEntity)

    @Query("UPDATE scans SET isSynced = 1 WHERE id = :scanId")
    suspend fun markAsSynced(scanId: Int)
    
    @Query("SELECT * FROM scans WHERE barcode = :barcode LIMIT 1")
    suspend fun getScanByBarcode(barcode: String): ScanEntity?
    
    @Query("SELECT COUNT(*) FROM scans WHERE unit = :unit AND part = :part")
    suspend fun isSlotFilled(unit: Int, part: String): Int
}
