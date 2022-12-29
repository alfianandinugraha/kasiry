package com.kasiry.app.theme

import androidx.compose.ui.graphics.Color

val BlueColor = mapOf(
    Pair(50, Color(0xFFCFE4F2)),
    Pair(100, Color(0xFFBFDBEE)),
    Pair(200, Color(0xFF9FC9E6)),
    Pair(300, Color(0xFF7FB6DD)),
    Pair(400, Color(0xFF5EA4D5)),
    Pair(500, Color(0xFF3E92CC)),
    Pair(600, Color(0xFF2C74A6)),
    Pair(700, Color(0xFF20557A)),
    Pair(800, Color(0xFF14364D)),
    Pair(900, Color(0xFF091721)),
)

val RedColor = mapOf(
    Pair(50, Color(0xFFFAB9B5)),
    Pair(100, Color(0xFFF9A6A2)),
    Pair(200, Color(0xFFF7817C)),
    Pair(300, Color(0xFFF45D56)),
    Pair(400, Color(0xFFF2382F)),
    Pair(500, Color(0xFFE9190F)),
    Pair(600, Color(0xFFB4130C)),
    Pair(700, Color(0xFF800E08)),
    Pair(800, Color(0xFF4B0805)),
    Pair(900, Color(0xFF160201)),
)

val BlackColor = mapOf(
    Pair(500, Color(0xFF221E22))
)

val GrayColor = mapOf(
    Pair(50, Color(0xFFFFFFFF)),
    Pair(100, Color(0xFFFFFFFF)),
    Pair(200, Color(0xFFFDFDFD)),
    Pair(300, Color(0xFFE9E9E9)),
    Pair(400, Color(0xFFD4D4D4)),
    Pair(500, Color(0xFFC0C0C0)),
    Pair(600, Color(0xFFA4A4A4)),
    Pair(700, Color(0xFF888888)),
    Pair(800, Color(0xFF6C6C6C)),
    Pair(900, Color(0xFF505050)),
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