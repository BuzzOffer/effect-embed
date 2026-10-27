package com.example.effectembed.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.effectembed.R
import com.example.effectembed.ui.theme.displayFontFamily

@Composable
fun HomeScreen(
    onEmbedClicked: () -> Unit,
    onExtractClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF0C351E),
                Color(0xFF27340E)
            )
        )),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.effect_embed_logo),
                contentDescription = "App Logo",
                modifier = Modifier.size(100.dp),
                tint = Color.White,
            )
            Text(
                "EffectEmbed",
                fontSize = 48.sp,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = displayFontFamily,
                color = Color.White,
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { onEmbedClicked() },
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF53A541)
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.embed_logo),
                    contentDescription = "Extract logo",
                    modifier = Modifier.size(40.dp),
                )
                Text(
                    "Embed",
                    fontSize = 36.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Button(
                onClick = { onExtractClicked() },
                modifier = Modifier.padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF53A541)
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.extract_logo),
                    contentDescription = "Extract logo",
                    modifier = Modifier.size(40.dp),
                )
                Text(
                    "Extract",
                    fontSize = 36.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        onExtractClicked = {},
        onEmbedClicked = {}
    )
}