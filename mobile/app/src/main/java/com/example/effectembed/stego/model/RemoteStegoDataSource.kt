package com.example.effectembed.stego.model

import com.example.effectembed.core.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteStegoDataSource @Inject constructor(private val apiService: StegoApiService) {
    suspend fun getEffects(): Result<NetworkResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getEffects()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Server error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun createStegoLog(userId: String, effectName: String, type: String, timeTaken: Number): Result<NetworkResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val requestData = StegoLogCreation(
                    userId, effectName, type, timeTaken
                )
                val response = apiService.createStegoLog(requestData)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Server error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}