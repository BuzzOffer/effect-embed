package com.example.effectembed.auth.viewmodels

import android.graphics.Bitmap
import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectembed.auth.model.IAuthRepository
import com.example.effectembed.auth.model.UserInfo
import com.example.effectembed.auth.model.UserInfoSerializer
import com.example.effectembed.core.utils.Resource
import com.example.effectembed.core.utils.extractUserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: IAuthRepository): ViewModel() {
    val userInfo: StateFlow<UserInfo?> = repository.currentUser
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserInfoSerializer.defaultValue
    )

    val emailState = TextFieldState()
    val passwordState = TextFieldState()

    private var _loginState  = MutableStateFlow<Resource<UserInfo?>>(Resource.Idle)
    val loginState = _loginState.asStateFlow()

    private var _errorText = MutableStateFlow("")
    val errorText = _errorText.asStateFlow()

    fun login() {
        val email = emailState.text.toString()
        val password = passwordState.text.toString()
        _loginState.value = Resource.Loading
        viewModelScope.launch {
            try {
                val result = repository.loginUser(email, password)
                result.fold(
                    onSuccess = { response ->
                        if (response.success) {
                            _loginState.value = Resource.Success(extractUserInfo(response.data))
                        } else {
                            _loginState.value = Resource.Failure(response.message)
                            _errorText.value = response.message
                        }
                    },
                    onFailure = { exception ->
                        _loginState.value = Resource.Failure(exception.toString())
                        _errorText.value = exception.toString()
                    }
                )
            } catch (e: Exception) {
                _loginState.value = Resource.Failure("Failed to login user")
                _errorText.value = "Login Failed"
                throw Exception(e)
            }
        }
    }
}