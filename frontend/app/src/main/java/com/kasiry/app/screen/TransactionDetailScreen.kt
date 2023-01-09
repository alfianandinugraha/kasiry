package com.kasiry.app.screen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kasiry.app.compose.Alert
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.Layout
import com.kasiry.app.compose.TopBar
import com.kasiry.app.models.data.Transaction
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.theme.red
import com.kasiry.app.utils.double.toFormattedString
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TransactionDetailScreen(
    application: Application,
    transactionId: String,
    transactionViewModel: TransactionViewModel,
    navController: NavController
) {
    var transactionState by remember {
        mutableStateOf<HttpState<Transaction>>(HttpState.Loading())
    }
    var isAlertOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        Log.d("TransactionDetailScreen", "Open!")
        transactionViewModel.detail(transactionId) {
            onSuccess {
                transactionState = it
                Log.d("TransactionDetailScreen", it.toString())
            }

            onError {
                Toast
                    .makeText(application.applicationContext, "Gagal memuat detail transaksi", Toast.LENGTH_LONG)
                    .show()
                navController.popBackStack()
            }
        }
    }

    if (isAlertOpen) {
        Alert(
            title = "Hapus Transaksi",
            icon = Icons.Rounded.Delete,
            onClose = {
                isAlertOpen = false
            }
        ) {
            Column {
                Text(
                    text = "Apakah anda yakin ingin menghapus data ini? Stok barang akan di kembalikan",
                    style = Typo.body,
                    textAlign = TextAlign.Center,
                )
                Button(
                    bgColor = { Color.red() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    onClick = {
                        val scope = CoroutineScope(Dispatchers.IO)

                        scope.launch {
                            transactionViewModel.delete(transactionId) {
                                onSuccess {
                                    isAlertOpen = false
                                    navController.popBackStack()
                                    Toast
                                        .makeText(application.applicationContext, "Transaksi berhasil dihapus", Toast.LENGTH_LONG)
                                        .show()
                                }

                                onError {
                                    Toast
                                        .makeText(application.applicationContext, "Gagal menghapus transaksi", Toast.LENGTH_LONG)
                                        .show()
                                    isAlertOpen = false
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        text = "Hapus",
                        style = Typo.body,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                    )
                }
                Text(
                    text = "Batal",
                    style = Typo.body,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            isAlertOpen = false
                        }
                        .padding(vertical = 14.dp),
                )
            }

        }
    }

    Layout(
        topbar = {
            TopBar(
                title = "Detail Transaksi",
                onBack = {
                    navController.popBackStack()
                },
            )
        },
    ) {
        when (val transaction = transactionState) {
            is HttpState.Success -> {
                item {
                    val capital = remember(transaction.data.products) {
                        transaction.data.products.sumOf { it.buyPrice * it.quantity }
                    }

                    val total = remember(transaction.data.products, capital) {
                        transaction.data.products.sumOf { it.sellPrice * it.quantity }
                    }

                    val profit = remember(total, capital) {
                        total - capital
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Tanggal",
                                style = Typo.body,
                                color = Color.gray(),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "28 Januari 2023",
                                style = Typo.body,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Jam",
                                style = Typo.body,
                                color = Color.gray(),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "08:23",
                                style = Typo.body,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Row(
                            modifier = Modifier
                                .padding(top = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "Total",
                                    style = Typo.body,
                                    color = Color.gray(),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Rp${total.toFormattedString()}",
                                    style = Typo.body,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.blue()
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(1f)
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
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.blue()
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Produk",
                        style = Typo.body,
                        color = Color.gray(),
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(bottom = 10.dp)
                    )
                }

                items(transaction.data.products.size) {
                    val product = transaction.data.products[it]

                    Row(
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .padding(bottom = 14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .size(50.dp)
                        ) {
                            AsyncImage(
                                model = product.picture?.url,
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                        }
                        Row (
                            modifier = Modifier
                                .padding(start = 14.dp)
                                .fillMaxSize()
                                .size(50.dp),
                            horizontalArrangement = SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${product.quantity}x ${product.name}",
                                style = Typo.body,
                            )
                            Column(
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "${product.quantity} x Rp${product.sellPrice.toFormattedString()}",
                                    style = Typo.body,
                                    fontSize = 14.sp,
                                    color = Color.gray()
                                )
                                Text(
                                    text = "Rp${(product.quantity * product.sellPrice).toFormattedString()}",
                                    style = Typo.body,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Hapus Transaksi",
                        style = Typo.body,
                        color = Color.red(),
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .fillMaxWidth()
                            .clickable {
                                isAlertOpen = true
                            },
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}