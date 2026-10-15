package com.example.effectembed.stego.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.effectembed.core.utils.Resource
import com.example.effectembed.stego.domain.StegoEngine
import com.example.effectembed.stego.effects.CollageStego
import com.example.effectembed.stego.effects.GlitchStego
import com.example.effectembed.stego.effects.StegoEffect
import com.example.effectembed.stego.model.StegoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.measureTimedValue

@HiltViewModel
class ExtractViewModel @Inject constructor(private val repository: StegoRepository) : ViewModel() {
    private var _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage = _selectedImage.asStateFlow()

    private var _passkey = MutableStateFlow("")
    val passkey = _passkey.asStateFlow()

    private var _selectedFilter = MutableStateFlow<StegoEffect?>(null)
    val selectedFilter = _selectedFilter.asStateFlow()

    private var _extractedMessage = MutableStateFlow("")
    val extractedMessage = _extractedMessage.asStateFlow()

    private var _statusMessage = MutableStateFlow("")
    val statusMessage = _statusMessage.asStateFlow()

    private var _extractState = MutableStateFlow<Resource<String?>>(Resource.Idle)
    val extractState = _extractState.asStateFlow()

    val availableEffects: List<StegoEffect> = listOf(
        GlitchStego(),
        CollageStego(),
    )

    fun setImage(image: Bitmap) {
        _selectedImage.value = image
    }

    fun setFilter(effect: StegoEffect) {
        _selectedFilter.value = effect
    }

    fun setPasskey(passkey: String) {
        _passkey.value = passkey
    }

    fun onExtract() {
        Log.d("EXTRACT", "Extract pressed!")
        _extractState.value = Resource.Loading
        viewModelScope.launch {
            try {
                if (selectedFilter.value != null) {
                    Log.d("EXTRACT", "Initializing engine...")
                    val stegoEngine = StegoEngine(selectedFilter.value!!)
                    val (resultMessage, timeTaken) = measureTimedValue {
                        stegoEngine.extractData(selectedImage.value, passkey.value)
                    }
                    Log.d("EXTRACT", "Extraction successful! $resultMessage")
                    _extractedMessage.value = resultMessage ?: ""
                    _statusMessage.value = "Successful extraction"
                    _extractState.value = Resource.Success(resultMessage)
                    val submitLogResult = repository.logProcess(_selectedFilter.value?.id ?: "none", "extract", timeTaken.inWholeMilliseconds)
                    submitLogResult.onFailure {
                        Log.e("STEGO LOG", "Error sending data to database")
                    }
                } else {
                    _extractedMessage.value = ""
                    _statusMessage.value = "No filter selected"
                    _extractState.value = Resource.Failure("No filter selected")
                }
            } catch (e: Exception) {
                Log.e("EXTRACT", "Exception caught!", e)
                _statusMessage.value = "Something went wrong"
                _extractState.value = Resource.Failure("Something went wrong", e)
            }
        }
    }

    fun resetState() {
        _extractState.value = Resource.Idle
    }

    fun submitLog() {

    }
}