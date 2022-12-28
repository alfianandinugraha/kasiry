package com.kasiry.app.theme

import androidx.compose.ui.graphics.Color

val BlueColor = mapOf(
    Pair(200, Color(0xFF9FC9E6)),
    Pair(300, Color(0xFF7FB6DD)),
    Pair(500, Color(0xFF3E92CC)),
)

val RedColor = mapOf(
    Pair(500, Color(0xFFE9190F)),
)

val BlackColor = mapOf(
    Pair(500, Color(0xFF221E22))
)

val GrayColor = mapOf(
    Pair(500, Color(0xFFC0C0C0))
)

fun Color.Companion.blue(value: Number = 500): Color {
    return BlueColor[value]!!
}

fun Color.Companion.red(value: Number = 500): Color {
    return RedColor[value]!!
}

fun Color.Companion.black(value: Number = 500): Color {
    return BlackColor[value]!!
}

fun Color.Companion.gray(value: Number = 500): Color {
    return GrayColor[value]!!
}