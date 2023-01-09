package com.kasiry.app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Outbound
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue

@Composable
fun TitleMore(
    text: String,
    modifier: Modifier = Modifier,
    onMore: () -> Unit = {}
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = text,
            style = Typo.body,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(999.dp)
                )
                .clickable {
                    onMore()
                }
                .background(Color.blue())
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Semua",
                style = Typo.body,
                fontSize = 14.sp,
                color = Color.White,
            )
            Icon(
                imageVector = Icons.Filled.Outbound,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(16.dp)
            )
        }
    }

}