package com.example.effectembed.auth.model

import com.example.effectembed.core.network.NetworkResponse
import retrofit2.Response
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login")
    suspend fun loginUser(): Response<NetworkResponse>

    @POST("auth/register")
    suspend fun registerUser(): Response<NetworkResponse>
}