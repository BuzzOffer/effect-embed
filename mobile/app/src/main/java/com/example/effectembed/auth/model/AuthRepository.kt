package com.example.effectembed.auth.model

import androidx.datastore.core.DataStore
import com.example.effectembed.core.network.NetworkResponse
import com.example.effectembed.core.utils.extractUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import android.util.Log

interface IAuthRepository {
    val currentUser: Flow<UserInfo?>
    suspend fun loginUser(email: String, password: String): Result<NetworkResponse>
}

class AuthRepository @Inject constructor(
    private val remoteDataSource: RemoteAuthDataSource,
    private val dataStore: DataStore<UserInfo>
) : IAuthRepository {
    override val currentUser: Flow<UserInfo?> = dataStore.data.map { userInfo ->
        if (userInfo.id.isBlank()) null else userInfo
    }

    override suspend fun loginUser(email: String, password: String): Result<NetworkResponse> {
        val result = remoteDataSource.loginUser(email, password)

        return result.fold(
            onSuccess = { response ->
                if (response.success) {
                    val userInfo = extractUserInfo(response.data)
                    if (userInfo != null) {
                        dataStore.updateData { info ->
                            userInfo
                        }
                        Result.success(response)
                    } else {
                        Result.failure(Exception(response.message))
                    }
                } else {
                    Result.failure(Exception(response.message))
                }
            },
            onFailure = {exception ->
                Result.failure(exception)
            }
        )
    }
}