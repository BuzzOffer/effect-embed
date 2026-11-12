package com.example.effectembed.auth.model

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val dataStore: DataStore<UserInfo>
) {
    suspend fun getUserInfo(): UserInfo {
        return dataStore.data
            .catch { exception ->
                throw exception
            }
            .first()
    }

    suspend fun getAuthToken(): String? {
        return getUserInfo().token.takeIf { it.isNotBlank() }
    }
}