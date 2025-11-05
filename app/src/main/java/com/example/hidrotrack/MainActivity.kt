package com.example.hidrotrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.hidrotrack.ui.AppNavigation
import com.example.hidrotrack.ui.theme.HidroTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HidroTrackTheme {
                AppNavigation()
            }
        }
    }
}
