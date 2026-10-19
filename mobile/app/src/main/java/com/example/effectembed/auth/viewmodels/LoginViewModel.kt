package com.example.effectembed.auth.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectembed.auth.model.IAuthRepository
import com.example.effectembed.auth.model.UserInfo
import com.example.effectembed.auth.model.UserInfoSerializer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val repository: IAuthRepository): ViewModel() {
    val userInfo: StateFlow<UserInfo?> = repository.currentUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserInfoSerializer.defaultValue
    )

    suspend fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.loginUser(email, password)
        }
    }
}