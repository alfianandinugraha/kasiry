package com.kasiry.app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kasiry.app.theme.blue

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    disabled: Boolean = false,
    bgColor: (disabled: Boolean) -> Color = { Color.blue(if (disabled) 300 else 500) },
    content: @Composable () -> Unit
) {
    val ripple = rememberRipple(bounded = false)
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(6.dp))
            .background(color = bgColor(disabled))
            .padding(14.dp)
            .clickable(indication = ripple, interactionSource = remember { MutableInteractionSource() }, enabled = !disabled) {
                if (onClick != null) {
                    onClick()
                }
            },
        horizontalArrangement = Arrangement.Center,
    ) {
        content()
    }
}