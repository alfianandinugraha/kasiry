package com.kasiry.app.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kasiry.app.models.data.Category
import com.kasiry.app.theme.*

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onClick: ((Category) -> Unit)? = null,
    withMore: Boolean = true,
    selected: Boolean = false,
) {
    val iconColor by animateColorAsState(
        targetValue = if (selected) Color.blue()
        else Color.gray(400)
    )

    val bgColor by animateColorAsState(
        targetValue = if (selected) Color.blue(50)
        else Color.White
    )

    val borderColor by animateColorAsState(
        targetValue = if (selected) Color.blue(500)
        else Color.gray(400)
    )

    val textColor by animateColorAsState(
        targetValue = if (selected) Color.blue(500)
        else Color.Black
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .background(bgColor)
            .clickable(
                indication = rememberRipple(bounded = false),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (onClick != null) {
                    onClick(category)
                }
            }
            .border(
                width = 1.dp,
                color = borderColor,
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
            tint = iconColor
        )
        Text(
            text = category.name,
            style = Typo.body,
            modifier = Modifier.padding(start = 12.dp),
            color = textColor
        )
        if (withMore) {
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
}