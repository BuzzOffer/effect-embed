package com.example.effectembed.stego.model

data class StegoLogCreation(
    val userId: String,
    val effectName: String,
    val type: String,
    val timeTaken: Number
)

data class EffectDetail(
    val id: String,
    val name: String,
    val avgTime: Number,
    val avgResults: Any,
    val createdAt: String
)