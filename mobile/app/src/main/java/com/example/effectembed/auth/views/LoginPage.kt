package com.example.effectembed.auth.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.effectembed.R
import com.example.effectembed.auth.model.UserInfo
import com.example.effectembed.core.utils.Resource
import com.example.effectembed.ui.theme.displayFontFamily
@Composable
fun LoginPage(
    onNavigateToRegister: () -> Unit,
    onLoginClicked: () -> Unit,
    goToHome: () -> Unit,
    loginState: Resource<UserInfo?>,
    errorText: String,
    emailState: TextFieldState,
    passwordState: TextFieldState
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
        verticalArrangement = Arrangement.Center,
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
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White.copy(alpha = 0.2f))
                .padding(16.dp),
        ) {
            Text(
                "Welcome Back",
                fontSize = 28.sp,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = displayFontFamily,
                color = Color.White,
            )
            OutlinedTextField(
                state = emailState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text("Email", color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                leadingIcon = { Icon(painter = painterResource(R.drawable.outline_email_24), contentDescription = "Email logo", tint = Color.White)}
            )
            OutlinedTextField(
                state = passwordState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                label = { Text("Password", color = Color.White) },
                textStyle = TextStyle(color = Color.White),
                leadingIcon = { Icon(painter = painterResource(R.drawable.outline_password_24), contentDescription = "Password logo", tint = Color.White)},
            )
            Button(
                onClick = { onLoginClicked() },
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF53A541)
                )
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_login_24),
                    contentDescription = "Login Logo",
                    modifier = Modifier.size(30.dp),
                )
                Text(
                    "Login",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(
                text = buildAnnotatedString {
                    append("First time user? ")
                    val clickableLink = LinkAnnotation.Clickable(
                        tag = "REGISTER_PAGE",
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = Color(0xFF6CF667)
                            )
                        ),
                        linkInteractionListener = {
                            onNavigateToRegister()
                        }
                    )
                    withLink(clickableLink) {
                        append("Sign up")
                    }
                },
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(16.dp)
            )
            if (errorText != "") {
                Text(
                    text = errorText,
                    color = Color.Red,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
    when (loginState) {
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
        is Resource.Success -> {
            goToHome()
        }
        is Resource.Failure -> {

        }
    }
}

@Preview (showBackground = true)
@Composable
fun PreviewLoginPage() {
    LoginPage(
        onNavigateToRegister = {},
        onLoginClicked = {},
        goToHome = {},
        loginState = Resource.Idle,
        errorText = "",
        emailState = TextFieldState(initialText = "hawyee@gmail.com"),
        passwordState = TextFieldState(initialText = "hawyee123")
    )
}