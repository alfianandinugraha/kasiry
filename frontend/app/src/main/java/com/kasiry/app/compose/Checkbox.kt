package com.kasiry.app.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kasiry.app.theme.*
import com.kasiry.app.utils.FormStore

@Composable
fun Checkbox(
    control: FormStore,
    name: String,
    label: String,
    modifier: Modifier = Modifier,
) {
    val field = remember {
        control.getField<Boolean>(name)
    }

    val value = field.value

    val icon = if (value) {
        Icons.Filled.CheckBox
    } else {
        Icons.Outlined.CheckBoxOutlineBlank
    }

    val iconColor by animateColorAsState(
        targetValue = if (value) Color.blue() else Color.gray(500)
    )
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                field.value = !value
            },
    ) {
        Box {
           Icon(
               icon,
               contentDescription = null,
               tint = iconColor
           )
        }
        Text(
            text = label,
            style = Typo.body,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}