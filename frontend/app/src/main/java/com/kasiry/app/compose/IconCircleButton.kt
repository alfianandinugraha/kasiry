package com.kasiry.app.compose

import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kasiry.app.theme.blue

@Composable
fun IconCircleButton(
    icon: ImageVector,
    contentDescription: String? = null,
    onClick: () -> Unit
) {
    val ripple = rememberRipple(bounded = false)
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = Color.White,
        modifier = Modifier
            .shadow(4.dp, RoundedCornerShape(50))
            .clip(RoundedCornerShape(50))
            .background(Color.blue())
            .clickable(
                indication = ripple, interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .padding(20.dp)
            .size(28.dp)
    )
}