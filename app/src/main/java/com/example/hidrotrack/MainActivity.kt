package com.example.hidrotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hidrotrack.ui.theme.HidroTrackTheme
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HidroTrackTheme {
                HidrotrackApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HidrotrackApp() {
    var count by rememberSaveable { mutableStateOf(0) }
    var goal by rememberSaveable { mutableStateOf(8) }
    val progress = (count.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hidrotrack") }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { count++ }) {
                Text("+1 vaso")
            }
        }
    ) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(p)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                progress = progress,
                strokeWidth = 10.dp,
                modifier = Modifier.size(180.dp)
            )
            Text(
                text = "$count / $goal Vasos consumidos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { if (count > 0) count-- }, enabled = count > 0) { Text("-1") }
                Button(onClick = { count++ }) { Text("+1") }
            }
            Text("Meta diaria")
            Slider(
                value = goal.toFloat(),
                onValueChange = { goal = it.toInt() },
                valueRange = 4f..20f,
                steps = 16
            )
            Button(onClick = { }) {
                Text("Guardar meta")
            }
        }
    }
}
