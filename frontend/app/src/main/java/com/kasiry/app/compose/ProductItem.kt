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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.kasiry.app.models.data.Product
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

val formatter = DecimalFormat("#,###.###", DecimalFormatSymbols().apply {
    decimalSeparator = ','
    groupingSeparator = '.'
})

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    onClick: ((Product) -> Unit)? = null,
    product: Product
) {
    val sellPrice = remember(product.sellPrice) {
        formatter.format(product.sellPrice)
    }

    val stock = remember(product.stock) {
        formatter.format(product.stock)
    }

    Column(
        modifier = modifier
            .clickable(
                enabled = onClick != null,
            ) {
                onClick?.invoke(product)
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
            text = product.name,
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
                    text = "$stock ${product.weight ?: ""}",
                    style = Typo.body,
                    color = Color.gray(),
                    fontSize = 14.sp
                )
            }
            Text(
                text = "Rp${sellPrice}",
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                color = Color.blue(),
            )
        }
    }
}