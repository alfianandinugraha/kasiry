package com.kasiry.app.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kasiry.app.compose.*
import com.kasiry.app.models.data.Profile
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.viewmodel.CartViewModel
import kotlin.math.ceil

@Composable
fun DashboardScreen(
    navController: NavController,
    profile: Profile,
    cartViewModel: CartViewModel
) {
    val cartState by cartViewModel.carts.collectAsState()

    LaunchedEffect(Unit) {
        cartViewModel.getAll()
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
                                    Text(
                                        text = "Rp150.000",
                                        style = Typo.body,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
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
                                    Text(
                                        text = "Rp100.000",
                                        style = Typo.body,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                    )
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
                                color = Color.blue()
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

                        Column(
                            modifier = Modifier
                                .padding(
                                    top = 28.dp,
                                )
                        ) {
                            TitleMore(text = "Transaksi Terakhir")
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            ) {
                                TransactionItem()
                                TransactionItem()
                                TransactionItem()
                                TransactionItem()
                                TransactionItem()
                            }
                        }

                        TitleMore(
                            text = "Produk",
                            modifier = Modifier
                                .padding(top = 20.dp, bottom = 12.dp)
                        )
                    }
                }
            }
        }
    }
}