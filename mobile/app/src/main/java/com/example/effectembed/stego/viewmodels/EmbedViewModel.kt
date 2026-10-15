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
class EmbedViewModel @Inject constructor(private val repository: StegoRepository): ViewModel() {
    private var _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage = _selectedImage.asStateFlow()
    private var _secretMessage = MutableStateFlow("")
    val secretMessage = _secretMessage.asStateFlow()

    private var _passkey = MutableStateFlow("")
    val passkey = _passkey.asStateFlow()

    private var _selectedFilter = MutableStateFlow<StegoEffect?>(null)
    val selectedFilter = _selectedFilter.asStateFlow()
    private var _embeddedImage = MutableStateFlow<Bitmap?>(null)
    val embeddedImage = _embeddedImage.asStateFlow()

    private var _resultText = MutableStateFlow("")
    val resultText = _resultText.asStateFlow()

    private var _embedState = MutableStateFlow<Resource<Bitmap?>>(Resource.Idle)
    val embedState = _embedState.asStateFlow()

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

    fun setMessage(message: String) {
        _secretMessage.value = message
    }

    fun setPasskey(passkey: String) {
        _passkey.value = passkey
    }

    fun onEmbed() {
        _embedState.value = Resource.Loading
        viewModelScope.launch {
            try {
                if (selectedFilter.value != null) {
                    val stegoEngine = StegoEngine(selectedFilter.value!!)
                    val (resultImage, timeTaken) = measureTimedValue {
                        stegoEngine.embedData(selectedImage.value, secretMessage.value, passkey.value)
                    }
                    _embeddedImage.value = resultImage
                    _resultText.value = "Successfully embed image!"
                    _embedState.value = Resource.Success(resultImage)

                    val submitLogResult = repository.logProcess(_selectedFilter.value?.id ?: "none", "embed", timeTaken.inWholeMilliseconds)
                    submitLogResult.onFailure { exception ->
                        Log.e("STEGO LOG", "Error sending data to database: ${_selectedFilter.value?.id}, ${timeTaken.inWholeMilliseconds}", exception)
                    }
                } else{
                    _resultText.value = "No filter selected"
                    _embedState.value = Resource.Failure("No filter selected")
                }
            } catch (e: Exception) {
                _resultText.value = e.message ?: "Something went wrong"
                _embedState.value = Resource.Failure("Something went wrong", e)
            }
        }
    }

    fun resetState() {
        _embedState.value = Resource.Idle
    }

    fun submitLog() {

    }
}