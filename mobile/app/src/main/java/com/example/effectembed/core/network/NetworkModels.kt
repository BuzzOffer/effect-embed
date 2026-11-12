package com.example.effectembed.core.network

import com.google.gson.JsonElement
data class NetworkResponse(
    val success: Boolean,
    val message: String,
    val data: JsonElement? = null
)