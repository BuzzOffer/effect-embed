package com.example.effectembed.stego.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.effectembed.core.utils.Resource
import com.example.effectembed.stego.effects.StegoEffect

@Composable
fun ExtractSettingsScreen(
    embeddedImage: Bitmap?,
    effects: List<StegoEffect>,
    selectedEffect: StegoEffect?,
    onEffectSelected: (StegoEffect) -> Unit,
    passkey: String,
    updatePasskey: (String) -> Unit,
    onExtractClicked: () -> Unit,
    currentState: Resource<String?>,
    goToResult: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Extract",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(20.dp)
        )
        embeddedImage?.let { image ->
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = "Selected embedded image",
                modifier = Modifier
                    .size(400.dp)
                    .padding(20.dp)
            )
        }
        EffectPicker(
            effects = effects,
            selectedEffect = selectedEffect,
            onEffectSelected = onEffectSelected
        )
        TextField(
            value = passkey,
            onValueChange = { updatePasskey(it) },
            label = { Text("Enter passkey (if applicable)") }
        )
        Button(
            onClick = { onExtractClicked() },
            modifier = Modifier
                .padding(5.dp),
        ) {
            Text("Extract")
        }
    }
    when (currentState) {
        is Resource.Idle -> {

        }
        is Resource.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable(enabled = false) {}, // Blocks ghost clicks
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
        else -> {
            goToResult()
        }
    }
}