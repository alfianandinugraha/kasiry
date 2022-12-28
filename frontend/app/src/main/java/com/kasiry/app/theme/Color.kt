package com.kasiry.app.theme

import androidx.compose.ui.graphics.Color

val Blue = mapOf(
    Pair(500, Color(0xFF3E92CC)),
)

val Red = mapOf(
    Pair(500, Color(0xFFE9190F)),
)

val Gray = mapOf(
    Pair(500, Color(0xFFC0C0C0))
)

val Black = mapOf(
    Pair(500, Color(0xFF221E22))
)

val BlueColor = mapOf(
    Pair(500, Color(0xFF3E92CC)),
)

val RedColor = mapOf(
    Pair(500, Color(0xFFE9190F)),
)

fun Color.Companion.blue(value: Number): Color {
    return BlueColor[value]!!
}

fun Color.Companion.red(value: Number): Color {
    return RedColor[value]!!
}