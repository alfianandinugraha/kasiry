package com.kasiry.app.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AttachMoney
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.ripple.rememberRipple
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
import com.kasiry.app.compose.*
import com.kasiry.app.models.data.Cart
import com.kasiry.app.models.data.Product
import com.kasiry.app.repositories.TransactionRepository
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.theme.red
import com.kasiry.app.utils.double.toFormattedString
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.CartViewModel
import com.kasiry.app.viewmodel.ProductViewModel
import com.kasiry.app.viewmodel.TransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

interface ProductCart {
    val product: Product
    val cart: Cart?
}

@Composable
fun CartListScreen(
    application: Application,
    cartViewModel: CartViewModel,
    productViewModel: ProductViewModel,
    navController: NavController,
    transactionViewModel: TransactionViewModel
) {
    val cartState by cartViewModel.carts.collectAsState()
    val productsState by productViewModel.listState.collectAsState()
    var isAlertOpen by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        productViewModel.getAll {
            onSuccess {
                val scope = CoroutineScope(Dispatchers.IO)

                scope.launch {
                    val cart = cartViewModel.getAll()
                    val result =
                        it
                            .data
                            .map {
                                object : ProductCart {
                                    override val product: Product = it
                                    override val cart: Cart? = cart.find { cart -> cart.product.productId == it.productId }
                                }
                            }
                            .filter {
                                it.cart != null
                            }
                            .map {
                                Cart(
                                    cartId = it.cart?.cartId ?: UUID.randomUUID().toString(),
                                    quantity = it.cart?.quantity ?: 0,
                                    product = it.product
                                )
                            }

                    cartViewModel.setCart(result)
                }
            }
        }
    }

    val totalPrice = remember(cartState) {
        cartState.sumOf {
            it.product.sellPrice * it.quantity
        }
    }

    if (isAlertOpen) {
        Alert(
            title = "Hapus Keranjang",
            icon = Icons.Rounded.Delete,
            onClose = {
                isAlertOpen = false
            }
        ) {
            Column {
                Text(
                    text = "Apakah anda yakin ingin menghapus semua data ini?",
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
                            cartViewModel.clear()
                            isAlertOpen = false
                        }
                    }
                ) {
                    Text(
                        text = "Hapus Semua",
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

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Layout(
            topbar = {
                TopBar(
                    title = "Keranjang",
                    onBack = {
                        navController.popBackStack()
                    },
                )
            },
        ) {
            when (val products = productsState) {
                is HttpState.Success -> {
                    val cart = cartState
                    items(
                        cartState.size,
                        key = {
                            cart[it].cartId
                        }
                    ) {
                        val item = cart[it]
                        val canDecrement = remember(item.quantity) {
                            item.quantity > 1.0
                        }
                        val canIncrement = remember(item.quantity, item.product.stock) {
                            item.quantity < item.product.stock
                        }

                        Row(
                            modifier = Modifier
                                .padding(bottom = 16.dp)
                                .padding(horizontal = 32.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(
                                        RoundedCornerShape(6.dp)
                                    )
                                    .background(Color.gray())
                                    .size(90.dp)
                            ) {
                                AsyncImage(
                                    model = item.product.picture?.url,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                )
                                Icon(
                                    Icons.Rounded.Delete,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier
                                        .clip(
                                            RoundedCornerShape(
                                                bottomEnd = 6.dp,
                                            )
                                        )
                                        .clickable(
                                            indication = rememberRipple(),
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            val scope = CoroutineScope(Dispatchers.IO)
                                            scope.launch {
                                                cartViewModel.delete(item.cartId)
                                            }
                                        }
                                        .background(Color.red())
                                        .padding(10.dp)
                                        .size(18.dp)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .padding(start = 12.dp)
                                    .fillMaxWidth()
                                    .height(90.dp),
                            ) {
                                Text(
                                    text = "${item.quantity}x ${item.product.name}",
                                    style = Typo.body,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Rp${(item.product.sellPrice * item.quantity).toFormattedString()}",
                                    style = Typo.body,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                ) {
                                    Spacer(modifier = Modifier.weight(1f))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(
                                            Icons.Rounded.Remove,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable(
                                                    enabled = canDecrement,
                                                ) {
                                                    val scope = CoroutineScope(Dispatchers.IO)
                                                    scope.launch {
                                                        cartViewModel.setQuantity(
                                                            item.cartId,
                                                            item.quantity - 1
                                                        )
                                                    }
                                                }
                                                .border(
                                                    width = 1.dp,
                                                    color = if (canDecrement) Color.blue() else Color.gray(),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(4.dp),
                                            tint = if (canDecrement) Color.blue() else Color.gray()
                                        )
                                        BasicTextField(
                                            value = item.quantity.toString(),
                                            onValueChange = {},
                                            modifier = Modifier
                                                .width(40.dp),
                                            textStyle = Typo.body.copy(
                                                textAlign = TextAlign.Center
                                            ),
                                        )
                                        Icon(
                                            Icons.Rounded.Add,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable(
                                                    enabled = canIncrement,
                                                ) {
                                                    val scope = CoroutineScope(Dispatchers.IO)
                                                    scope.launch {
                                                        cartViewModel.setQuantity(
                                                            item.cartId,
                                                            item.quantity + 1
                                                        )
                                                    }
                                                }
                                                .border(
                                                    width = 1.dp,
                                                    color = if (canIncrement) Color.blue() else Color.gray(),
                                                    shape = RoundedCornerShape(4.dp)
                                                )
                                                .padding(4.dp),
                                            tint = if (canIncrement) Color.blue() else Color.gray()
                                        )
                                    }

                                }
                            }
                        }
                    }

                    if (cart.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 32.dp)
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp),
                            ) {
                                Row(
                                    modifier = Modifier
                                        .clickable(
                                            indication = rememberRipple(),
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            isAlertOpen = true
                                        }
                                        .border(
                                            width = 1.dp,
                                            color = Color.red(),
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Rounded.Delete,
                                        contentDescription = null,
                                        tint = Color.red(),
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(end = 4.dp)
                                    )
                                    Text(
                                        text = "Hapus semua",
                                        style = Typo.body,
                                        color = Color.red(),
                                        fontSize = 14.sp,
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                            }
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
        when (val products = productsState) {
            is HttpState.Success -> {
                if (cartState.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth()
                            .align(Alignment.BottomStart),
                    ) {
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color.gray(300),
                                )
                                .height(70.dp)
                                .padding(start = 32.dp)
                                .weight(.6f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                            ) {
                                Text(
                                    text = "Total",
                                    modifier = Modifier,
                                    style = Typo.body,
                                    color = Color.gray()
                                )
                                Text(
                                    text = "Rp${totalPrice.toFormattedString()}",
                                    modifier = Modifier,
                                    style = Typo.body,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .height(70.dp)
                                .background(Color.blue())
                                .clickable(
                                    indication = rememberRipple(),
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    val scope = CoroutineScope(Dispatchers.IO)
                                    scope.launch {

                                        withContext(Dispatchers.Main) {
                                            transactionViewModel.store(
                                                TransactionRepository.StoreBody(
                                                    products = cartState.map {
                                                        TransactionRepository.ProductRequest(
                                                            productId = it.product.productId,
                                                            quantity = it.quantity,
                                                        )
                                                    },
                                                )
                                            )
                                            navController.popBackStack()
                                        }
                                        cartViewModel.clear()
                                    }
                                }
                                .weight(.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Icon(
                                Icons.Rounded.AttachMoney,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .size(22.dp)
                            )
                            Text(
                                text = "Bayar",
                                style = Typo.body,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
            else -> {}
        }
    }
}