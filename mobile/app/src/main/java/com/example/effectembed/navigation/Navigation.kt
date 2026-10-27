package com.example.effectembed.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.effectembed.stego.viewmodels.EmbedViewModel
import com.example.effectembed.stego.views.EmbedPickerScreen
import com.example.effectembed.stego.views.EmbedSettingsScreen
import com.example.effectembed.stego.views.ExtractPickerScreen
import kotlinx.serialization.Serializable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.effectembed.auth.views.LoginPage
import com.example.effectembed.auth.views.RegisterPage
import com.example.effectembed.composables.AppBar
import com.example.effectembed.home.HomeScreen
import com.example.effectembed.stego.viewmodels.ExtractViewModel
import com.example.effectembed.stego.views.EmbedResultScreen
import com.example.effectembed.stego.views.ExtractResultScreen
import com.example.effectembed.stego.views.ExtractSettingsScreen

@Serializable
object LoginPage

@Serializable
object RegisterPage
@Serializable
object Home

@Serializable
object EmbedPicker

@Serializable
object EmbedSettings

@Serializable
object EmbedResult

@Serializable
object ExtractPicker

@Serializable
object ExtractSettings

@Serializable
object ExtractResult
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val isHome = (currentDestination?.hasRoute<Home>() == true)
    Scaffold(
        topBar = {
            if (!isHome) {
                AppBar { navController.popBackStack() }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController,
            startDestination = LoginPage,
            modifier = Modifier
                .fillMaxSize()
                // 1. Apply the top padding so content doesn't hide under the AppBar
                .padding(top = innerPadding.calculateTopPadding())
                // 2. Apply the exact same padding to the bottom to restore true centering!
                .padding(bottom = innerPadding.calculateTopPadding())
        ) {
            composable<LoginPage> {
                LoginPage(
                    onNavigateToRegister = {navController.navigate(route = RegisterPage) }
                )
            }
            composable<RegisterPage> {
                RegisterPage(
                    onNavigateToLogin = { navController.navigate(route = LoginPage) }
                )
            }
            composable<Home> {
                HomeScreen(
                    onEmbedClicked = { navController.navigate(route = EmbedPicker) },
                    onExtractClicked = { navController.navigate(route = ExtractPicker) }
                )
            }
            composable<EmbedPicker> { backStackEntry ->
                val viewModel = hiltViewModel<EmbedViewModel>(viewModelStoreOwner = backStackEntry)
                EmbedPickerScreen(
                    onImageSelected = { bitmap ->
                        viewModel.setImage(bitmap)
                        navController.navigate(route = EmbedSettings)
                    }
                )
            }
            composable<EmbedSettings> { backStackEntry ->
                val pickerEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<EmbedPicker>()
                }
                val viewModel = hiltViewModel<EmbedViewModel>(viewModelStoreOwner = pickerEntry)
                EmbedSettingsScreen(
                    effects = viewModel.availableEffects,
                    coverImage = viewModel.selectedImage.collectAsStateWithLifecycle().value,
                    selectedEffect = viewModel.selectedFilter.collectAsStateWithLifecycle().value,
                    onEffectSelected = { effect ->
                        viewModel.setFilter(effect)
                    },
                    secretMessage = viewModel.secretMessage.collectAsStateWithLifecycle().value,
                    updateMessage = { viewModel.setMessage(it) },
                    passkey = viewModel.passkey.collectAsStateWithLifecycle().value,
                    updatePasskey = { viewModel.setPasskey(it) },
                    onEmbedClicked = {
                        viewModel.onEmbed()
                    },
                    currentState = viewModel.embedState.collectAsStateWithLifecycle().value,
                    goToResult = {
                        viewModel.resetState()
                        navController.navigate(route = EmbedResult)
                    }
                )
            }
            composable<EmbedResult> { backStackEntry ->
                val pickerEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<EmbedPicker>()
                }
                val viewModel = hiltViewModel<EmbedViewModel>(viewModelStoreOwner = pickerEntry)
                EmbedResultScreen(
                    embeddedImage = viewModel.embeddedImage.collectAsStateWithLifecycle().value,
                    resultMessage = viewModel.resultText.collectAsStateWithLifecycle().value,
                    onRetryClicked = {
                        viewModel.resetState()
                        navController.navigate(route = EmbedPicker)
                    },
                    onExtractClicked = {
                        viewModel.resetState()
                        navController.navigate(route = ExtractPicker)
                    },
                    onHomeClicked = {
                        viewModel.resetState()
                        navController.navigate(route = Home)
                    }
                )
            }
            composable<ExtractPicker> { backStackEntry ->
                val viewModel = hiltViewModel<ExtractViewModel>(viewModelStoreOwner = backStackEntry)
                ExtractPickerScreen(
                    onImageSelected = { bitmap ->
                        viewModel.setImage(bitmap)
                        navController.navigate(route = ExtractSettings)
                    }
                )
            }
            composable<ExtractSettings> { backStackEntry ->
                val pickerEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<ExtractPicker>()
                }
                val viewModel = hiltViewModel<ExtractViewModel>(viewModelStoreOwner = pickerEntry)
                ExtractSettingsScreen(
                    effects = viewModel.availableEffects,
                    embeddedImage = viewModel.selectedImage.collectAsStateWithLifecycle().value,
                    selectedEffect = viewModel.selectedFilter.collectAsStateWithLifecycle().value,
                    onEffectSelected = { effect ->
                        viewModel.setFilter(effect)
                    },
                    passkey = viewModel.passkey.collectAsStateWithLifecycle().value,
                    updatePasskey = { viewModel.setPasskey(it) },
                    onExtractClicked = {
                        viewModel.onExtract()
                    },
                    currentState = viewModel.extractState.collectAsStateWithLifecycle().value,
                    goToResult = {
                        viewModel.resetState()
                        navController.navigate(route = ExtractResult)
                    }
                )
            }
            composable<ExtractResult> { backStackEntry ->
                val pickerEntry = remember(backStackEntry) {
                    navController.getBackStackEntry<ExtractPicker>()
                }
                val viewModel = hiltViewModel<ExtractViewModel>(viewModelStoreOwner = pickerEntry)
                ExtractResultScreen(
                    embeddedImage = viewModel.selectedImage.collectAsStateWithLifecycle().value,
                    resultMessage = viewModel.extractedMessage.collectAsStateWithLifecycle().value,
                    statusMessage = viewModel.statusMessage.collectAsStateWithLifecycle().value,
                    onRetryClicked = {
                        viewModel.resetState()
                        navController.navigate(route = ExtractPicker)
                    },
                    onEmbedClicked = {
                        viewModel.resetState()
                        navController.navigate(route = EmbedPicker)
                    },
                    onHomeClicked = {
                        viewModel.resetState()
                        navController.navigate(route = Home)
                    }
                )
            }
        }
    }
}

