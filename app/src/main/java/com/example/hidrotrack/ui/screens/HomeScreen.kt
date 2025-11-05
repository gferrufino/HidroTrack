package com.example.hidrotrack.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.hidrotrack.LocalApp
import com.example.hidrotrack.ui.MainVMFactory
import com.example.hidrotrack.ui.MainViewModel
import com.example.hidrotrack.ui.components.ConfettiOverlay
import com.example.hidrotrack.ui.components.WaterBackground
import com.example.hidrotrack.ui.components.WaterStyledTitle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: MainViewModel = viewModel(factory = MainVMFactory(LocalApp.app))
) {
    val todayCount by vm.todayCount.collectAsState()
    val goal by vm.goalFlow.collectAsState(initial = 8)
    val progress = (todayCount.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f)

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isSaving by remember { mutableStateOf(false) }

    val haptics = LocalHapticFeedback.current

    var showConfetti by remember { mutableStateOf(false) }
    var prevProgress by remember { mutableStateOf(progress) }
    LaunchedEffect(progress) {
        if (prevProgress < 1f && progress >= 1f) {
            showConfetti = true
        }
        prevProgress = progress
    }

    val context = LocalContext.current
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            scope.launch { snackbarHostState.showSnackbar("Foto guardada en la galería") }
        } else {
            scope.launch { snackbarHostState.showSnackbar("No se pudo tomar la foto") }
        }
    }

    val reachedGoal = goal > 0 && todayCount >= goal

    Box(Modifier.fillMaxSize()) {
        WaterBackground(progress = progress)

        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { WaterStyledTitle("Hidrotrack") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    actions = {
                        IconButton(onClick = { navController.navigate("history") }) {
                            Icon(Icons.Filled.History, contentDescription = "Historial", tint = Color(0xFF0288D1))
                        }
                        IconButton(onClick = { navController.navigate("settings") }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Ajustes", tint = Color(0xFF0288D1))
                        }
                    }
                )
            }