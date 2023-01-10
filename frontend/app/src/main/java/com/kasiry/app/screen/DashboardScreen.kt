package com.kasiry.app.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kasiry.app.compose.*
import com.kasiry.app.models.data.Product
import com.kasiry.app.models.data.Profile
import com.kasiry.app.models.data.Transaction
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.utils.double.toFormattedString
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.CartViewModel
import com.kasiry.app.viewmodel.ProductViewModel
import com.kasiry.app.viewmodel.SummaryViewModel
import com.kasiry.app.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.ceil

@Composable
fun DashboardScreen(
    navController: NavController,
    profile: Profile,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    transactionViewModel: TransactionViewModel,
    summaryViewModel: SummaryViewModel
) {
    val cartState by cartViewModel.carts.collectAsState()
    val summaryState by summaryViewModel.summaryState.collectAsState()

    var transactionsState by remember {
        mutableStateOf<HttpState<List<Transaction>>>(HttpState.Loading())
    }
    var productsState by remember {
        mutableStateOf<HttpState<List<Product>>>(HttpState.Loading())
    }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        cartViewModel.getAll()
    }

    LaunchedEffect(Unit) {
        productViewModel.getAll(limit = 6) {
            onSuccess {
                productsState = it
                Log.d("DasboardScreen", it.data.toString())
            }

            onError {
                Toast
                    .makeText(
                        context,
                        "Gagal memuat produk terbaru",
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    LaunchedEffect(Unit) {
        transactionViewModel.getAll(limit = 5) {
            onSuccess {
                transactionsState = it
            }

            onError {
                Toast
                    .makeText(
                        context,
                        "Gagal memuat transaksi terakhir",
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    LaunchedEffect(Unit) {
        summaryViewModel.get() {
            onSuccess {
                Log.d("DasboardScreen", it.data.toString())
            }

            onError {
                Toast
                    .makeText(
                        context,
                        "Gagal memuat ringkasan",
                        Toast.LENGTH_LONG
                    )
                    .show()
            }
        }
    }

    Layout(
        floatingButton = {
            if (cartState.isNotEmpty()) {
                IconCircleButton(
                    icon = Icons.Rounded.ShoppingCart,
                    contentDescription = "Keranjang",
                    onClick = {
                        navController.navigate("cart")
                    }
                )
            }
        }
    ) {
        item {
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            Color.blue()
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                ) {
                    Column() {
                        Column(
                            modifier = Modifier
                                .shadow(8.dp)
                                .clip(
                                    shape = RoundedCornerShape(
                                        bottomEnd = 8.dp,
                                        bottomStart = 8.dp,
                                    ),
                                )
                                .height(150.dp)
                                .background(Color.White)
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column() {
                                    Text(
                                        text = "Selamat datang,",
                                        style = Typo.body,
                                        fontSize = 14.sp,
                                        color = Color.gray()
                                    )
                                    Text(
                                        text = profile.name,
                                        style = Typo.body,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Rounded.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .clip(
                                            shape = RoundedCornerShape(999.dp)
                                        )
                                        .background(Color.blue())
                                        .padding(8.dp)
                                        .size(20.dp)
                                        .clickable {
                                            navController.navigate("profile")
                                        }
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Penjualan",
                                        style = Typo.body,
                                        fontSize = 14.sp,
                                        color = Color.gray()
                                    )
                                    when(val summary = summaryState) {
                                        is HttpState.Success -> {
                                            Text(
                                                text = "Rp${summary.data.total.toFormattedString()}",
                                                style = Typo.body,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                        else -> {
                                            Text(
                                                text = "Rp-",
                                                style = Typo.body,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp)
                                ) {
                                    Text(
                                        text = "Keuntungan",
                                        style = Typo.body,
                                        fontSize = 14.sp,
                                        color = Color.gray()
                                    )
                                    when(val summary = summaryState) {
                                        is HttpState.Success -> {
                                            Text(
                                                text = "Rp${summary.data.profit.toFormattedString()}",
                                                style = Typo.body,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                        else -> {
                                            Text(
                                                text = "Rp-",
                                                style = Typo.body,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 28.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            MainMenu(
                                text = "Produk",
                                icon = Icons.Rounded.Inventory2,
                                bgColor = Color.blue(50),
                                color = Color.blue(),
                                onClick = {
                                    navController.navigate("products")
                                }
                            )
                            MainMenu(
                                text = "Transaksi",
                                icon = Icons.Rounded.ShoppingCart,
                                bgColor = Color.blue(50),
                                color = Color.blue(),
                                onClick = {
                                    navController.navigate("transactions")
                                }
                            )
                            MainMenu(
                                text = "Pegawai",
                                icon = Icons.Rounded.People,
                                bgColor = Color.blue(50),
                                color = Color.blue(),
                                onClick = {
                                    navController.navigate("employees")
                                }
                            )
                            MainMenu(
                                text = "Kategori",
                                icon = Icons.Rounded.Label,
                                bgColor = Color.blue(50),
                                color = Color.blue(),
                                onClick = {
                                    navController.navigate("categories")
                                }
                            )
                            MainMenu(
                                text = "Usaha",
                                icon = Icons.Rounded.Store,
                                bgColor = Color.blue(50),
                                color = Color.blue(),
                                onClick = {
                                    navController.navigate("company")
                                }
                            )
                        }
                    }
                }
            }
        }

        item {
            TitleMore(
                text = "Transaksi Terakhir",
                modifier = Modifier
                    .padding(
                        horizontal = 32.dp
                    )
                    .padding(
                        top = 28.dp,
                        bottom = 12.dp
                    ),
                onMore = {
                    navController.navigate("transactions")
                }
            )
        }

        when (val transactions = transactionsState) {
            is HttpState.Success -> {
                items(transactions.data.size) {
                    val transaction = transactions.data[it]

                    TransactionItem(
                        transaction = transaction,
                        modifier = Modifier
                            .padding(horizontal = 32.dp),
                        onClick = {
                            navController.navigate("transactions/${transaction.transactionId}")
                        }
                    )
                }
            }
            else -> {}
        }

        item {
            TitleMore(
                text = "Produk Terbaru",
                modifier = Modifier
                    .padding(
                        horizontal = 32.dp
                    )
                    .padding(
                        top = 16.dp,
                        bottom = 12.dp
                    ),
                onMore = {
                    navController.navigate("products")
                }
            )
        }

        when (val products = productsState) {
            is HttpState.Success -> {
                val data = products.data
                items(ceil(data.size / 2f).toInt(), key = { data[it].productId }) { index ->
                    val firstData = data[index * 2]
                    val secondData = data.getOrNull((index * 2) + 1)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .padding(bottom = 16.dp),
                    ) {
                        ProductItem(
                            modifier = Modifier
                                .weight(1f),
                            product = firstData,
                            onUpdate = if (profile.abilities.product == true) {
                                {
                                    navController.navigate("products/${firstData.productId}/update")
                                }
                            } else {
                                null
                            },
                            onSubmitCart = {
                                val scope = CoroutineScope(Dispatchers.IO)

                                scope.launch {
                                    cartViewModel.getAll()

                                    cartViewModel.store(
                                        CartViewModel.StoreBody(
                                            quantity = it.quantity,
                                            product = firstData
                                        )
                                    )
                                }
                            }
                        )
                        if (secondData != null) {
                            ProductItem(
                                modifier = Modifier
                                    .weight(1f),
                                product = secondData,
                                onUpdate = if (profile.abilities.product == true) {
                                    {
                                        navController.navigate("products/${firstData.productId}/update")
                                    }
                                } else {
                                    null
                                },
                                onSubmitCart = {
                                    val scope = CoroutineScope(Dispatchers.IO)

                                    scope.launch {
                                        cartViewModel.getAll()

                                        cartViewModel.store(
                                            CartViewModel.StoreBody(
                                                quantity = it.quantity,
                                                product = secondData
                                            )
                                        )
                                    }
                                }
                            )
                        } else {
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}