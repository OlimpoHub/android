package com.app.arcabyolimpo.presentation.common.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

/**
 * A composable that displays a shimmering loading placeholder effect.
 *
 * This view is typically used while content is being loaded (e.g., during API calls)
 * to indicate that data is being fetched. The shimmer animation gives a sense of motion,
 * making loading states feel more dynamic and responsive.
 *
 * The shimmer effect is achieved by animating a linear gradient across the surface,
 * simulating light reflection over a placeholder area.
 *
 * @param modifier [Modifier] used to adjust the layout or styling of the shimmer container
 * (e.g., size, padding, shape, or alignment).
 *
 * ### Example Usage:
 * ```
 * LoadingShimmer(
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .height(120.dp)
 * )
 * ```
 *
 * ### UI Behavior:
 * - The shimmer is drawn using a [Brush.linearGradient] that continuously moves diagonally.
 * - Colors are derived from `MaterialTheme.colorScheme.surfaceVariant` with varying alpha values
 *   to create the illusion of light reflection.
 * - The shimmer loops infinitely using an animated float transition.
 * - Wrapped inside a [Surface] with rounded corners and a slight elevation for depth.
 *
 * ### Animation Details:
 * - Duration: 1000ms per cycle.
 * - Repeat mode: [RepeatMode.Restart].
 * - Uses an infinite repeatable transition for seamless looping.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun LoadingShimmer(modifier: Modifier = Modifier) {
    val shimmerColors =
        listOf(
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
        )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(1000),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "",
        )

    val brush =
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnim.value, y = translateAnim.value),
        )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = 1.dp,
    ) {
        Spacer(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(brush),
        )
    }
}
