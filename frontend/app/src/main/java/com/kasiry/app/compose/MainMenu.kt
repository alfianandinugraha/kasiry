package com.kasiry.app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue

@Composable
fun MainMenu(
    text: String,
    icon: ImageVector,
    bgColor: Color,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(55.dp).clickable(onClick = { onClick?.invoke() })
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(8.dp)
                )
                .background(
                    bgColor
                )
                .padding(8.dp)
                .size(28.dp)
        )
        Text(
            text = text,
            style = Typo.body,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(top = 4.dp)
        )
    }
}