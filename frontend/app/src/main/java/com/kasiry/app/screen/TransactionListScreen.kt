package com.kasiry.app.screen

import android.app.Application
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kasiry.app.compose.Layout
import com.kasiry.app.compose.Loading
import com.kasiry.app.compose.TopBar
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.theme.red
import com.kasiry.app.utils.double.toFormattedString
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.TransactionViewModel

@Composable
fun TransactionListScreen(
    application: Application,
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    val listState by transactionViewModel.listState.collectAsState()

    LaunchedEffect(Unit) {
        transactionViewModel.getAll() {
            onSuccess {
                Log.d("TransactionListScreen", "onSuccess: $it")
            }
        }
    }

    Layout(
        topbar = {
            TopBar(
                title = "Transaksi",
                onBack = {
                    navController.popBackStack()
                },
            )
        },
    ) {
        when(val list = listState) {
            is HttpState.Success -> {
                items(list.data.size) {
                    val transaction = list.data[it]
                    val firstProduct = transaction.products.first()

                    val capital = remember(transaction.products) {
                        transaction.products.sumOf { it.buyPrice * it.quantity }
                    }

                    val total = remember(transaction.products, capital) {
                        transaction.products.sumOf { it.sellPrice * it.quantity }
                    }

                    val profit = remember(total, capital) {
                        total - capital
                    }

                    Row(
                        modifier = Modifier
                            .clickable(
                                indication = rememberRipple(),
                                interactionSource = remember {
                                    MutableInteractionSource()
                                }
                            ) {

                            }
                            .padding(
                                horizontal = 32.dp
                            )
                            .padding(
                                top = 10.dp,
                                bottom = 10.dp
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(8.dp)
                                )
                                .background(Color.gray())
                                .size(80.dp)
                        ) {
                            AsyncImage(
                                model = firstProduct.picture.url,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(
                                    start = 16.dp
                                )
                        ) {
                            Text(
                                text = "28 Januari 2022",
                                style = Typo.body,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "${transaction.products.size} produk",
                                style = Typo.body,
                            )
                            Column(
                                modifier = Modifier
                                    .padding(top = 8.dp)
                            ) {
                                Column {
                                    Text(
                                        text = "Total",
                                        style = Typo.body,
                                        color = Color.gray(),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Rp${total.toFormattedString()}",
                                        style = Typo.body,
                                        color = Color.blue()
                                    )
                                }
                               Column(
                                   modifier = Modifier.padding(top = 8.dp)
                               ) {
                                   Text(
                                       text = "Keuntungan",
                                       style = Typo.body,
                                       color = Color.gray(),
                                       fontSize = 14.sp
                                   )
                                   Text(
                                       text = "Rp${profit.toFormattedString()}",
                                       style = Typo.body,
                                       color = Color.blue()
                                   )
                               }
                            }
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Rounded.ChevronRight,
                            contentDescription = null,
                            tint = Color.blue(),
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                }
            }
            is HttpState.Loading -> {
                item {
                    Loading()
                }
            }
            else -> {}
        }
    }
}