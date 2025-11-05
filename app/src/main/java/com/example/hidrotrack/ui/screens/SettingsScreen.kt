package com.example.hidrotrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.hidrotrack.LocalApp
import com.example.hidrotrack.ui.MainVMFactory
import com.example.hidrotrack.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    vm: MainViewModel = viewModel(factory = MainVMFactory(LocalApp.app))
) {
    val goal by vm.goalFlow.collectAsState(initial = 8)
    var tempGoal by remember { mutableStateOf(goal) }

    var goalText by remember { mutableStateOf("") }
    val goalInt = goalText.toIntOrNull()
    val goalError = goalText.isNotBlank() && (goalInt == null || goalInt !in 4..20)

    LaunchedEffect(goal) {
        tempGoal = goal
        goalText = goal.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(p)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text("Configurar meta diaria (slider)", style = MaterialTheme.typography.titleMedium)
            Slider(
                value = tempGoal.toFloat(),
                onValueChange = { tempGoal = it.toInt() },
                valueRange = 4f..20f,
                steps = 16
            )
            Text("Meta actual (slider): $tempGoal vasos")
            Button(onClick = { vm.setGoal(tempGoal) }) { Text("Guardar meta (slider)") }

            Divider()

            Text("Configurar meta manualmente", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = goalText,
                onValueChange = { input ->
                    // Permite solo números
                    if (input.isEmpty() || input.all { it.isDigit() }) {
                        goalText = input
                    }
                },
                label = { Text("Meta diaria (4–20)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = goalError,
                supportingText = {
                    if (goalError) Text("Ingresa un número entre 4 y 20")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = { vm.setGoal(goalInt!!) },
                enabled = goalInt != null && goalInt in 4..20
            ) {
                Text("Guardar meta (manual)")
            }
        }
    }
}
