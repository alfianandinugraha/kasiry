package com.kasiry.app.compose

import androidx.annotation.Dimension
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.red

@Composable
fun ErrorMessage(message: String) {
    Row(modifier = Modifier.padding(top = 4.dp)) {
        Icon(
            Icons.Rounded.Warning,
            contentDescription = "",
            tint = Color.red(500),
            modifier = Modifier
                .width(14.dp)
                .padding(top = 2.dp)
        )
        Text(
            text = message,
            fontSize = 14.sp,
            color = Color.red(500),
            style = Typo.body,
            modifier = Modifier
                .padding(start = 4.dp),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )
    }
}