package com.example.hidrotrack.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun ConfettiOverlay(
    show: Boolean,
    onFinished: () -> Unit,
    durationMs: Int = 1600,
    particles: Int = 120
) {
    if (!show) return

    val density = LocalDensity.current
    val items = remember {
        val rand = Random(42)
        List(particles) {
            ConfettiParticle(
                x0 = rand.nextFloat(),
                sizeDp = (4 + rand.nextInt(10)).dp,
                speed = 0.7f + rand.nextFloat() * 0.8f,
                wobble = 8f + rand.nextFloat() * 18f,
                tilt = -30f + rand.nextFloat() * 60f,
                color = confettiPalette[rand.nextInt(confettiPalette.size)]
            )
        }
    }

    val fall by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMs, easing = LinearEasing),
        label = "confettiFall"
    )

    val infinite = rememberInfiniteTransition(label = "confettiSpin")
    val spin by infinite.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing)
        ),
        label = "spin"
    )

    LaunchedEffect(Unit) {
        delay(durationMs.toLong() + 50)
        onFinished()
    }

    Canvas(Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        items.forEach { p ->
            val x = p.x0 * w +
                    (p.wobble * sin(fall * 8.0).toFloat())

            val y = fall * h * p.speed
            val px = with(density) { p.sizeDp.toPx() }

            rotate(p.tilt + spin, pivot = Offset(x, y)) {
                drawRect(
                    color = p.color,
                    topLeft = Offset(x - px / 2f, y - px / 2f),
                    size = androidx.compose.ui.geometry.Size(px, px * 0.6f)
                )
            }
        }
    }
}

private data class ConfettiParticle(
    val x0: Float,
    val sizeDp: Dp,
    val speed: Float,
    val wobble: Float,
    val tilt: Float,
    val color: Color
)

private val confettiPalette = listOf(
    Color(0xFF4FC3F7),
    Color(0xFF29B6F6),
    Color(0xFF81C784),
    Color(0xFFFFF176),
    Color(0xFFFFB74D),
    Color(0xFFE57373),
    Color(0xFFBA68C8)
)
