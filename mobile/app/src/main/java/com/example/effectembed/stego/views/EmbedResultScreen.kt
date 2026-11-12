package com.example.effectembed.stego.views

import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.effectembed.core.utils.saveToGallery
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun EmbedResultScreen(
    embeddedImage: Bitmap?,
    resultMessage: String,
    onRetryClicked: () -> Unit,
    onExtractClicked: () -> Unit,
    onHomeClicked: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
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
        embeddedImage?.let { image ->
            Image(
                bitmap = image.asImageBitmap(),
                contentDescription = "Embedded image",
                modifier = Modifier
                    .size(400.dp)
                    .padding(20.dp)
            )
        }
        Button(
            onClick = {
                embeddedImage?.let { bitmap ->
                    coroutineScope.launch {
                        val isSuccess = saveToGallery(context, bitmap)
                        var toastMessage = "Failed to save image."
                        if (isSuccess) {
                            toastMessage = "Saved to Gallery!"
                        }
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .padding(5.dp),
        ) {
            Text("Save Image")
        }
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
        ) {
            Text(
                text = resultMessage,
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
        }
        Button(
            onClick = { onRetryClicked() },
            modifier = Modifier
                .padding(5.dp),
        ) {
            Text("Retry")
        }
        Button(
            onClick = { onExtractClicked() },
            modifier = Modifier
                .padding(5.dp),
        ) {
            Text("Extract")
        }
        Button(
            onClick = { onHomeClicked() },
            modifier = Modifier
                .padding(5.dp),
        ) {
            Text("Home")
        }
    }
}