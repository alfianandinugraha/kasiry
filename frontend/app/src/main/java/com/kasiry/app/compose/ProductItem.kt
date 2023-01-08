package com.kasiry.app.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.kasiry.app.models.data.Cart
import com.kasiry.app.models.data.Product
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.ceil
import kotlin.math.floor

val formatter = DecimalFormat("#,###.###", DecimalFormatSymbols().apply {
    decimalSeparator = ','
    groupingSeparator = '.'
})

@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    onClick: ((Product) -> Unit)? = null,
    product: Product,
    onUpdate: ((Product) -> Unit)? = null,
    onSubmitCart: (Cart) -> Unit = {}
) {
    val sellPrice = remember(product.sellPrice) {
        formatter.format(product.sellPrice)
    }

    val buyPrice = remember(product.buyPrice) {
        formatter.format(product.buyPrice)
    }

    val stock = remember(product.stock) {
        formatter.format(product.stock)
    }

    var isModalOpen by remember { mutableStateOf(false) }

    if (isModalOpen) {
        val form = remember {
            FormStore(
                fields = mapOf(
                    "quantity" to Field(
                        initialValue = "1",
                    )
                )
            )
        }
        val quantityField = form.getField<String>("quantity")

        ModalBottom(title = "Produk", onClose = { isModalOpen = false }) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(8.dp)
                            )
                            .size(80.dp)
                            .background(Color.gray(300))
                    ) {
                        AsyncImage(
                            model = product.picture?.url,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .fillMaxHeight()
                    ) {
                        Text(
                            text = product.name,
                            style = Typo.body,
                            fontSize = 16.sp,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
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
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Ubah",
                            style = Typo.body,
                            color = Color.blue(),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .clickable {
                                    isModalOpen = false
                                    onUpdate?.invoke(product)
                                }
                        )
                    }
                }
                Row {
                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = "Harga Beli",
                            style = Typo.body,
                            color = Color.gray(),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Rp. $buyPrice",
                            style = Typo.body,
                            fontSize = 16.sp
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = "Harga Jual",
                            style = Typo.body,
                            color = Color.gray(),
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Rp. $sellPrice",
                            style = Typo.body,
                            fontSize = 16.sp
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(top = 32.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        CartCounter(icon = Icons.Rounded.Remove)
                        BasicTextField(
                            value = quantityField.value,
                            onValueChange = {
                                quantityField.value = it
                            },
                            modifier = Modifier.weight(1f),
                            textStyle = Typo.body.copy(
                                textAlign = TextAlign.Center,
                            ),
                        )
                        CartCounter(icon = Icons.Rounded.Add)
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            onSubmitCart(
                                Cart(
                                    quantity = quantityField.value.toDouble(),
                                    cartId = "cart-id",
                                    product = product,
                                )
                            )
                            isModalOpen = false
                        },
                    ) {
                        Text(
                            text = "Tambah ke Keranjang",
                            style = Typo.body,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }

    Column(
        modifier = modifier
            .clickable() {
                isModalOpen = true
            }
    ) {
        Box(
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(8.dp)
                )
                .height(120.dp)
                .background(Color.gray(300))
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = product.picture?.url,
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
                .padding(top = 4.dp)
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

@Composable
private fun CartCounter(
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Icon(
        icon,
        contentDescription = null,
        modifier = modifier
            .size(32.dp)
            .border(
                width = 1.dp,
                color = Color.blue(),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp),
        tint = Color.blue()
    )
}