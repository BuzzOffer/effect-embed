package com.example.effectembed.stego.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.effectembed.core.utils.Resource
import com.example.effectembed.navigation.EmbedResult
import com.example.effectembed.stego.effects.StegoEffect

@Composable
fun EmbedSettingsScreen(
    coverImage: Bitmap?,
    effects: List<StegoEffect>,
    selectedEffect: StegoEffect?,
    onEffectSelected: (StegoEffect) -> Unit,
    secretMessage: String,
    updateMessage: (String) -> Unit,
    passkey: String,
    updatePasskey: (String) -> Unit,
    onEmbedClicked: () -> Unit,
    currentState: Resource<Bitmap?>,
    goToResult: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Embed",
            fontSize = 32.sp,
            modifier = Modifier
                .padding(20.dp)
        )
        coverImage?.let { image ->
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = "Selected cover image",
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
            value = secretMessage,
            onValueChange = { updateMessage(it) },
            label = { Text("Enter message") }
        )
        TextField(
            value = passkey,
            onValueChange = { updatePasskey(it) },
            label = { Text("Enter passkey (optional)") }
        )
        Button(
            onClick = { onEmbedClicked() },
            modifier = Modifier
                .padding(5.dp),
        ) {
            Text("Embed")
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