package com.example.bluescan.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.bluescan.data.local.AppDatabase
import com.example.bluescan.data.remote.NetworkModule
import com.example.bluescan.data.remote.ScanRequest

class SyncWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val dao = AppDatabase.getDatabase(applicationContext).scanDao()
        val api = NetworkModule.api
        
        val unsynced = dao.getUnsyncedScans()
        if (unsynced.isEmpty()) return Result.success()

        Log.d("SyncWorker", "Found ${unsynced.size} unsynced scans")

        for (scan in unsynced) {
            try {
                val request = ScanRequest(
                    projectId = scan.projectId,
                    date = scan.date,
                    technicianId = scan.technicianId,
                    unit = scan.unit,
                    part = scan.part,
                    barcode = scan.barcode
                )
                
                val response = api.submitScan(request)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "SUCCESS" || body?.status == "UNIT_COMPLETED" || body?.status == "DUPLICATE") {
                        // Marked as synced even if duplicate, to stop retrying forever?
                        // Or maybe Duplicate means we should flag it. 
                        // For now, assume if server handled it, we are synced.
                        dao.markAsSynced(scan.id)
                    }
                }
            } catch (e: Exception) {
                // Retry later
                return Result.retry()
            }
        }

        return Result.success()
    }
}
