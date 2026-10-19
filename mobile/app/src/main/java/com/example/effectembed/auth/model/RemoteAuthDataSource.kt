package com.example.effectembed.auth.model

import com.example.effectembed.core.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RemoteAuthDataSource @Inject constructor(private val apiService: AuthApiService) {
    suspend fun loginUser(email: String, password: String): Result<NetworkResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val requestData = LoginCredentials(
                    email, password
                )
                val response = apiService.loginUser(requestData)
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

    suspend fun registerUser(username: String, email: String, password: String): Result<NetworkResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val requestData = RegisterCredentials(
                    username, email, password, "user"
                )
                val response = apiService.registerUser(requestData)
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