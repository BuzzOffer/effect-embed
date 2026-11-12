package com.example.effectembed.auth.model

import kotlinx.serialization.Serializable

data class LoginCredentials(
    val email: String,
    val password: String
)

data class RegisterCredentials(
    val username: String,
    val email: String,
    val password: String,
    val role: String
)