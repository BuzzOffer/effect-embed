package com.example.effectembed.stego.model

import android.util.Log
import androidx.datastore.core.DataStore
import com.example.effectembed.auth.model.SessionManager
import com.example.effectembed.auth.model.UserInfo
import com.example.effectembed.core.network.NetworkResponse
import javax.inject.Inject


interface IStegoRepository {
    suspend fun fetchEffects(): Result<NetworkResponse>
    suspend fun logProcess(effectName: String, type: String, timeTaken: Number): Result<NetworkResponse>
}

class StegoRepository @Inject constructor(
    private val remoteDataSource: RemoteStegoDataSource,
    private val sessionManager: SessionManager
) :
    IStegoRepository {
        override suspend fun fetchEffects(): Result<NetworkResponse> {
            val token = sessionManager.getUserInfo().token
            return remoteDataSource.getEffects(token)
        }

        override suspend fun logProcess(effectName: String, type: String, timeTaken: Number): Result<NetworkResponse> {
            val token = sessionManager.getUserInfo().token
            val authHeader = "Bearer $token"
            Log.d("TOKEN", token)
            val userId = sessionManager.getUserInfo().id
            return remoteDataSource.createStegoLog(authHeader, userId, effectName, type, timeTaken)
        }
}