package com.example.bluescan.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/scan")
    suspend fun submitScan(@Body request: ScanRequest): Response<ScanResponse>
}

data class ScanRequest(
    @SerializedName("project_id") val projectId: String,
    val date: String,
    @SerializedName("technician_id") val technicianId: String,
    val unit: Int,
    val part: String,
    val barcode: String
)

data class ScanResponse(
    val status: String,
    val message: String?,
    @SerializedName("next_unit") val nextUnit: Int?
)
