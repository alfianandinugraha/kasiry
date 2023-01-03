package com.kasiry.app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue

@Composable
fun TopBar(
    back: Boolean = true,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    title: String? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .size(32.dp)
                .clickable {
                    if (onBack != null) {
                        onBack()
                    }
                }
        )
        if (title != null) {
            Text(
                text = title,
                modifier = Modifier.padding(start = 16.dp),
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}