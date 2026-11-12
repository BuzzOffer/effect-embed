package com.example.effectembed.stego.views

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExtractResultScreen(
    embeddedImage: Bitmap?,
    resultMessage: String,
    statusMessage: String,
    onRetryClicked: () -> Unit,
    onEmbedClicked: () -> Unit,
    onHomeClicked: () -> Unit
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
                contentDescription = "Embedded image",
                modifier = Modifier
                    .size(400.dp)
                    .padding(20.dp)
            )
        }
        Text(
            text = "Status: $statusMessage",
            fontSize = 14.sp,
            modifier = Modifier
                .padding(5.dp)
        )
        Spacer(modifier = Modifier.size(5.dp))
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .size(width = 240.dp, height = 100.dp)
        ) {
            Text(
                text = "Extracted text: ",
                modifier = Modifier
                    .padding(10.dp),
                textAlign = TextAlign.Left,
                fontSize = 12.sp
            )
            Text(
                text = resultMessage,
                modifier = Modifier
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
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
            onClick = { onEmbedClicked() },
            modifier = Modifier
                .padding(5.dp),
        ) {
            Text("Embed")
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