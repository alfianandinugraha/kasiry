package com.kasiry.app.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .clickable(
                enabled = onClick != null,
            ) {
                onClick?.invoke()
            }
    ) {
        Box(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(8.dp)
                )
                .height(120.dp)
                .background(Color.blue())
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = "https://images.pexels.com/photos/4498135/pexels-photo-4498135.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
        }
        Text(
            text = "Box Murah box murah box murah",
            style = Typo.body,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(top = 4.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Inventory2,
                    contentDescription = null,
                    tint = Color.gray(),
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 4.dp)
                )
                Text (
                    text = "3 unit",
                    style = Typo.body,
                    color = Color.gray(),
                    fontSize = 14.sp
                )
            }
            Text(
                text = "Rp100.000",
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                color = Color.blue(),
            )
        }
    }
}