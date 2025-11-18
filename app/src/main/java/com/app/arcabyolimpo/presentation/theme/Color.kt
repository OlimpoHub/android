package com.app.arcabyolimpo.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Base colors
val Background = Color(0xFF040610)
val White = Color(0xFFFFF7EB)

// Primary
val PrimaryBlue = Color(0xFF2844AE)
val SecondaryBlue = Color(0xFF011560)
val ButtonBlue = Color(0xFF3655C7)

// Semantic colors
val DangerGray = Color(0xFFDBD5CC)
val ErrorRed = Color(0xFFB3261E)
val HighlightRed = Color(0xFFD1323A)

// Inputs
val InputBackgroundRed = Color(0xFF2C0F0F)
val SelectInputBlue = Color(0xFF3253D1)
val InputBackgroundBlue = Color(0xFF0F142C)
val HighlightInputBlue = Color(0xFF3D59C2)
val PlaceholderGray = Color(0XFF87858B)

// Backgrounds
val HeaderBackground = Color(0xCE070A18)
val HeaderLineBlue = Color(0xFF101432)
val PopupBackgroundBlue = Color(0xFF1D1F28)

val gradientBrush =
    Brush.linearGradient(
        colors = listOf(ButtonBlue, SecondaryBlue),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
    )
