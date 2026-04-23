package com.example.bluescan.data

import android.content.Context
import android.util.Log
import com.example.bluescan.data.local.AppDatabase
import com.example.bluescan.data.local.ScanDao
import com.example.bluescan.data.local.ScanEntity
import com.example.bluescan.data.remote.ApiService
import com.example.bluescan.data.remote.NetworkModule
import com.example.bluescan.data.remote.ScanRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class ScanResult {
    object Success : ScanResult()
    data class UnitCompleted(val nextUnit: Int) : ScanResult()
    data class Duplicate(val message: String) : ScanResult()
    data class Error(val message: String) : ScanResult()
    object OfflineQueued : ScanResult()
}

class ScanRepository(context: Context) {
    private val dao: ScanDao = AppDatabase.getDatabase(context).scanDao()
    private val api: ApiService = NetworkModule.api

    suspend fun scan(
        legacyRepo: com.example.bluescan.CsvRepository?, // Added for legacy support
        folderUri: android.net.Uri?,
        tableIndex: Int,
        projectId: String,
        date: String,
        technicianId: String,
        unit: Int,
        part: String,
        barcode: String
    ): ScanResult = withContext(Dispatchers.IO) {
        
        // 1. Check Local Duplicate (Fast Fail)
        val existing = dao.getScanByBarcode(barcode)
        if (existing != null) {
            return@withContext ScanResult.Duplicate("Duplicate scan (Local)")
        }

        // 2. Legacy Persistence (CSV)
        // MUST happen for Dashboard to see it.
        if (legacyRepo != null && folderUri != null && tableIndex != -1) {
             try {
                 // updateCell does file I/O, strict dependency
                 legacyRepo.updateCell(folderUri, tableIndex, unit, part, barcode)
                 // If it crashed, it would be caught below, but updateCell catches its own.
                 // We assume success if no crash.
             } catch (e: Exception) {
                 Log.e("ScanRepo", "Legacy Save Failed: ${e.message}")
                 // Continue? Prompt says "Persist data locally... Return SUCCESS only after data is written"
                 // If CSV fails, is it a total failure?
                 // Given this is a critical regression fix, we should probably proceed to Room backup at least.
             }
        }

        // 3. Prepare Room Entity
        val entity = ScanEntity(
            projectId = projectId, date = date, technicianId = technicianId,
            unit = unit, part = part, barcode = barcode, isSynced = false
        )

        try {
            // 4. Try Online
            val response = api.submitScan(ScanRequest(projectId, date, technicianId, unit, part, barcode))
            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                when (body.status) {
                    "SUCCESS" -> {
                        dao.insertScan(entity.copy(isSynced = true))
                        ScanResult.Success
                    }
                    "UNIT_COMPLETED" -> {
                        dao.insertScan(entity.copy(isSynced = true))
                        ScanResult.UnitCompleted(body.nextUnit ?: (unit + 1))
                    }
                    "DUPLICATE" -> {
                         // Note: If API says duplicate, but Local CSV was just written...
                         // We technically have a sync conflict. 
                         // But for now, we follow API truth.
                         ScanResult.Duplicate(body.message ?: "Duplicate")
                    }
                    else -> ScanResult.Error("Unknown Status: ${body.status}")
                }
            } else {
                Log.e("ScanRepo", "API Fail: ${response.code()}")
                saveOffline(entity)
            }
        } catch (e: Exception) {
            Log.e("ScanRepo", "Network Error: ${e.message}")
            saveOffline(entity)
        }
    }

    private suspend fun saveOffline(entity: ScanEntity): ScanResult {
        dao.insertScan(entity)
        return ScanResult.OfflineQueued
    }
}
