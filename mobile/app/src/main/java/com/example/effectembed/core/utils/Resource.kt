package com.example.effectembed.core.utils

sealed interface Resource<out T> {
    data class Success<T>(val data: T): Resource<T>
    data class Failure(val message: String, val exception: Exception? = null): Resource<Nothing>
    data object Loading: Resource<Nothing>
    data object Idle: Resource<Nothing>
}