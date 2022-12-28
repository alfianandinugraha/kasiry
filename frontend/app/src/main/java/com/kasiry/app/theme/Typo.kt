package com.kasiry.app.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kasiry.app.R

val nunitoSansFamily = FontFamily(
    Font(R.font.nunitosans_regular, FontWeight.Normal),
    Font(R.font.nunitosans_bold, FontWeight.Bold),
)

class Typo {
    companion object {
        val body = TextStyle (
            fontFamily = nunitoSansFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
        )
    }
}