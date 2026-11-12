package com.example.effectembed.stego.model

import com.example.effectembed.core.network.NetworkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface StegoApiService {
    @GET("effects")
    suspend fun getEffects(
        @Header("Authorization") authHeader: String?
    ): Response<NetworkResponse>

    @POST("stego/create")
    suspend fun createStegoLog(
        @Header("Authorization") authHeader: String?,
        @Body reqBody: StegoLogCreation
    ): Response<NetworkResponse>
}