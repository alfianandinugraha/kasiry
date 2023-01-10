package com.kasiry.app.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kasiry.app.models.data.Transaction
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.utils.double.toFormattedString
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    transaction: Transaction,
    onClick: (() -> Unit)? = null
) {
    val total = remember {
        transaction.products.sumOf { it.quantity * it.sellPrice }
    }

    val date = remember(transaction.datetime) {
        SimpleDateFormat("dd MMMM yyyy")
            .format(
                Date(transaction.datetime.toLong() * 1000)
            )
    }

    Box(
        modifier = modifier
            .padding(
                bottom = 12.dp
            )
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onClick?.invoke()
                }
            )
    ) {
        Column {
            Text(
                text = "Rp${total.toFormattedString()}",
                style = Typo.body,
                color = Color.blue(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 2.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Rounded.CalendarToday,
                    contentDescription = null,
                    tint = Color.gray(),
                    modifier = Modifier
                        .size(16.dp)
                        .padding(end = 4.dp)
                )
                Text(
                    text = date,
                    style = Typo.body,
                    fontSize = 14.sp,
                    color = Color.gray(),
                )
            }
        }
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = null,
            tint = Color.blue(),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(18.dp)
        )
    }
}