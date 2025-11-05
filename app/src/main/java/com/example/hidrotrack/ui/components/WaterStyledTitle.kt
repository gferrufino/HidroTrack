package com.example.hidrotrack.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun WaterStyledTitle(text: String) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            brush = Brush.linearGradient(
                listOf(
                    Color(0xFF4FC3F7),
                    Color(0xFF29B6F6),
                    Color(0xFF0288D1)
                )
            )
        )
    )
}
