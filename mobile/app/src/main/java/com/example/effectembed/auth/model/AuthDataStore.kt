package com.example.effectembed.auth.model

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable data class UserInfo(val id: String, val username: String, val email: String, val token: String)

object UserInfoSerializer : Serializer<UserInfo> {
    override val defaultValue: UserInfo = UserInfo(id = "", username = "", email = "", token = "")

    override suspend fun readFrom(input: InputStream): UserInfo =
        try {
            Json.decodeFromString(UserInfo.serializer(), input.readBytes().decodeToString())
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read user info: ", serialization)
        }

    override suspend fun writeTo(t: UserInfo, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(Json.encodeToString(UserInfo.serializer(), t).encodeToByteArray())
        }
    }
}

val Context.dataStore: DataStore<UserInfo> by
    dataStore(
        fileName = "user_info.json",
        serializer = UserInfoSerializer,
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )