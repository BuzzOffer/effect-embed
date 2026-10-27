package com.example.effectembed.stego.model

import com.example.effectembed.core.network.NetworkResponse
import javax.inject.Inject


class StegoRepository @Inject constructor(private val remoteDataSource: RemoteStegoDataSource) {

    suspend fun fetchEffects(): Result<NetworkResponse> {
        return remoteDataSource.getEffects()
    }

    suspend fun logProcess(effectName: String, type: String, timeTaken: Number): Result<NetworkResponse> {
        val userId = "006b9b68-8a53-49b4-b205-9e91c7195d82"
        return remoteDataSource.createStegoLog(userId, effectName, type, timeTaken)
    }

}