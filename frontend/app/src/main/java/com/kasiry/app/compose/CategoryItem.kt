package com.kasiry.app.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kasiry.app.models.data.Category
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onClick: (Category) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                indication = rememberRipple(bounded = false),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick(category)
            }
            .border(
                width = 1.dp,
                color = Color.gray(300),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Icon(
            Icons.Rounded.Label,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically),
            tint = Color.gray(400)
        )
        Text(
            text = category.name,
            style = Typo.body,
            modifier = Modifier.padding(start = 12.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = Color.blue(),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}