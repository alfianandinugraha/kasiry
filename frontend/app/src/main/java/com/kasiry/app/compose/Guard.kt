package com.kasiry.app.compose

import androidx.compose.runtime.Composable
import com.kasiry.app.models.data.Ability

@Composable
fun Guard(
    allowed: String,
    abilities: List<String>,
    content: @Composable () -> Unit,
) {
    if (allowed in abilities || abilities.contains(Ability.SUPER)) {
        content()
    }
}