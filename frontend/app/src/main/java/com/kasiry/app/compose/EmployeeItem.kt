package com.kasiry.app.compose

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasiry.app.models.data.Employee
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray

@Composable
fun EmployeeItem(
    modifier: Modifier = Modifier,
    onClick: ((Employee) -> Unit)? = null,
    employee: Employee,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                indication = rememberRipple(bounded = false),
                interactionSource = remember { MutableInteractionSource() },
                enabled = onClick != null,
            ) {
                if (onClick != null) {
                    onClick(employee)
                }
            }
            .border(
                width = 1.dp,
                color = Color.gray(300),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        Icon(
            Icons.Rounded.Person, contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.CenterVertically),
            tint = Color.gray(400)
        )
        Column(
            modifier = Modifier.padding(start = 12.dp)
        ) {
            Text(
                text = employee.name,
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = employee.phone,
                style = Typo.body,
                fontSize = 14.sp,
                color = Color.gray()
            )
        }
        if (onClick != null) {
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