package com.example.effectembed.core.network

import kotlinx.serialization.json.JsonElement

data class NetworkResponse(
    val success: Boolean,
    val message: String,
    val data: JsonElement? = null
)