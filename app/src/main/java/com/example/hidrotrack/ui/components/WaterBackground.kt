package com.example.hidrotrack.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun WaterBackground(progress: Float, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(600),
        label = "waterProgress"
    )

    val infinite = rememberInfiniteTransition(label = "waveTransition")
    val phase by infinite.animateFloat(
        initialValue = 0f,
        targetValue = (2f * PI.toFloat()),
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wavePhase"
    )

    Box(modifier = modifier.fillMaxSize().background(Color.White)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val waterHeight = h * animatedProgress
            val topY = h - waterHeight

            val amplitude = 12.dp.toPx()
            val wavelength = w / 1.2f
            val yOffset = topY

            val path = Path().apply {
                moveTo(0f, h)
                lineTo(0f, yOffset)
                var x = 0f
                while (x <= w) {
                    val angle = (x / wavelength * 2f * PI.toFloat() + phase)
                    val y = yOffset + amplitude * sin(angle.toDouble()).toFloat()
                    lineTo(x, y)
                    x += 6f
                }
                lineTo(w, h)
                close()
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF4FC3F7), Color(0xFF1E88E5)),
                    startY = yOffset - 100f,
                    endY = h
                )
            )
        }
    }
}
