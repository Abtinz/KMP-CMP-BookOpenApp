package com.plcoding.bookpedia.core.presentation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * A Composable function that displays a pulsing animation effect.
 *
 * This animation consists of a circular border that repeatedly scales up and fades out,
 * creating a "pulse" or "ripple" effect. The animation runs indefinitely.
 *
 * The core of the animation is an `infiniteRepeatable` transition that animates a float
 * value from 0f to 1f over 1000 milliseconds. This value is then used to control the
 * scale (from 0 to 1) and alpha (from 1 to 0) of a `Box` with a circular border.
 *
 * @param modifier The [Modifier] to be applied to the animation container. This allows for
 * customization of size, padding, alignment, etc., from the call site.
 */
@Composable
fun PulseAnimation(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = progress
                scaleY = progress
                alpha = 1f - progress
            }
            .border(
                width = 5.dp,
                color = SandYellow,
                shape = CircleShape
            )
    )
}