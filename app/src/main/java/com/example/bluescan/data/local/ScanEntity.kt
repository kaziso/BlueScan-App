package com.example.bluescan.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: String,
    val date: String,
    val technicianId: String,
    val unit: Int,
    val part: String,
    val barcode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
)
