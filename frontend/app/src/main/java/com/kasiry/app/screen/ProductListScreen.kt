package com.kasiry.app.screen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.*
import com.kasiry.app.theme.gray
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.utils.rememberPermissionRequest
import com.kasiry.app.viewmodel.ProductViewModel
import kotlin.math.ceil

@Composable
fun ProductListScreen(
    navController: NavController,
    application: Application,
    productViewModel: ProductViewModel
) {
    val permission = rememberPermissionRequest("android.permission.CAMERA")
    val filterListState = productViewModel.filterListState.collectAsState()
    var isCameraOpen by remember {
        mutableStateOf(false)
    }
    val form = remember {
        FormStore(
            fields = mapOf(
                "query" to Field(
                    initialValue = ""
                )
            )
        )
    }
    LaunchedEffect(Unit) {
        productViewModel.getAll() {
            onSuccess {
                productViewModel.filter("")
                Log.d("ProductListScreen", "ProductListScreen: $it")
            }
            onError {
                Log.d("ProductListScreen", "ProductListScreen: $it")
            }
        }
    }

    if (isCameraOpen) {
        Box {
            CameraView(
                onFound = {
                    form.setValue("query", it)
                    isCameraOpen = false
                },
                onError = {
                    Toast
                        .makeText(
                            application.applicationContext,
                            "Gagal membaca QR Cdoe",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    isCameraOpen = false
                }
            )
            Box(
                modifier = Modifier
                    .offset(x = 16.dp, y = 16.dp)
                    .clip(
                        shape = RoundedCornerShape(999.dp)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    ) {
                        isCameraOpen = false
                    }
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Icon(
                    Icons.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .fillMaxHeight()
        )
        Layout(
            topbar = {
                TopBar(
                    title = "Produk",
                    onBack = {
                        navController.popBackStack()
                    },
                )
            },
            floatingButton = {
                IconCircleButton(
                    icon = Icons.Rounded.Add,
                    onClick = {
                        navController.navigate("products/create")
                    }
                )
            }
        ) {

            when (val list = filterListState.value) {
                is HttpState.Success -> {
                    item {
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(bottom = 12.dp)
                                    .height(IntrinsicSize.Min),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                TextField(
                                    label = "Kata kunci",
                                    control = form,
                                    name = "query",
                                    modifier = Modifier
                                        .weight(.8f),
                                )
                                Box(
                                    modifier = Modifier
                                        .clip(
                                            shape = RoundedCornerShape(999.dp)
                                        )
                                        .weight(.2f)
                                        .fillMaxWidth()
                                        .fillMaxHeight()
                                        .clickable(
                                            indication = rememberRipple(),
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            permission.launch {
                                                onGranted {
                                                    isCameraOpen = true
                                                }
                                                onDenied {
                                                    Toast
                                                        .makeText(
                                                            application.applicationContext,
                                                            "Gagal membuka kamera",
                                                            Toast.LENGTH_LONG
                                                        )
                                                        .show()
                                                }
                                            }
                                        },
                                ) {
                                    Icon(
                                        Icons.Rounded.QrCodeScanner,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(28.dp)
                                            .align(Alignment.Center),
                                    )
                                }
                            }
                        }
                    }

                    val data = list.data
                    items(ceil(data.size / 2f).toInt()) { index ->
                        val firstData = data[index * 2]
                        val secondData = data.getOrNull((index * 2) + 1)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            ProductItem(
                                modifier = Modifier
                                    .weight(1f),
                                product = firstData,
                            )
                            if (secondData != null) {
                                ProductItem(
                                    modifier = Modifier
                                        .weight(1f),
                                    product = secondData,
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
                else -> {
                    item {
                        Loading()
                    }
                }
            }

        }
    }
}