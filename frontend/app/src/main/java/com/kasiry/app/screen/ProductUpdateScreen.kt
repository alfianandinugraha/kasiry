package com.kasiry.app.screen

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kasiry.app.compose.*
import com.kasiry.app.models.data.Product
import com.kasiry.app.rules.required
import com.kasiry.app.theme.*
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.utils.rememberPermissionRequest
import com.kasiry.app.viewmodel.ProductViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProductUpdateScreen(
    navController: NavController,
    application: Application,
    productViewModel: ProductViewModel,
    productId: String
) {
    var productState by remember {
        mutableStateOf<HttpState<Product>>(HttpState.Loading())
    }

    val form = remember {
        FormStore(
            fields = mapOf(
                "name" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
                "description" to Field(
                    initialValue = "",
                ),
                "stock" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
                "sell_price" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
                "buy_price" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
                "barcode" to Field(
                    initialValue = "",
                ),
                "category_id" to Field(
                    initialValue = "",
                ),
                "picture_id" to Field(
                    initialValue = "",
                ),
            )
        )
    }
    val categoryField = form.getField<String>("category_id")
    var categoryName by remember {
        mutableStateOf("")
    }

    var imageUri by remember {
        mutableStateOf(Uri.EMPTY)
    }
    var isCameraOpen by remember {
        mutableStateOf(false)
    }
    var isModalOpen by remember {
        mutableStateOf(false)
    }
    var isCategoryOpen by remember {
        mutableStateOf(false)
    }
    var isAlertOpen by remember {
        mutableStateOf(false)
    }

    val permission = rememberPermissionRequest("android.permission.CAMERA")

    LaunchedEffect(Unit) {
        productViewModel.get(productId) {
            onSuccess {
                productState = it

                form.setValue(
                    "name", it.data.name
                )
                form.setValue(
                    "description", it.data.description ?: ""
                )
                form.setValue(
                    "stock", it.data.stock.toString()
                )
                form.setValue(
                    "sell_price", it.data.sellPrice.toString()
                )
                form.setValue(
                    "buy_price", it.data.buyPrice.toString()
                )
                form.setValue(
                    "barcode", it.data.barcode ?: ""
                )
                form.setValue(
                    "category_id", it.data.category?.categoryId ?: ""
                )
                categoryName = it.data.category?.name ?: ""

                if (it.data.picture !== null) {
                    form.setValue(
                        "picture_id", it.data.picture.assetId ?: ""
                    )
                    imageUri = it.data.picture.url.toUri()
                }
            }
            onError {
                productState = it
            }
        }
    }

    if (isCameraOpen) {
        CameraView(
            onFound = {
                form.setValue("barcode", it)
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
            },
            onClose = {
                isCameraOpen = false
            }
        )
    } else {
        if (isModalOpen) {
            ModalPicturePicker(
                onClose = {
                    isModalOpen = false
                },
                onFind = {
                    imageUri = it
                    isModalOpen = false
                }
            )
        }
        if (isAlertOpen) {
            Alert(
                title = "Hapus Produk",
                icon = Icons.Rounded.Delete,
                onClose = {
                    isAlertOpen = false
                }
            ) {
                Column {
                    Text(
                        text = "Apakah anda yakin ingin menghapus data ini?",
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
                                productViewModel.delete(productId) {
                                    onSuccess {
                                        isAlertOpen = false
                                        navController.popBackStack()
                                    }
                                    onError {
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
                    title = "Ubah Produk",
                    onBack = {
                        navController.popBackStack()
                    },
                )
            },
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 32.dp
                        )
                        .padding(
                            bottom = 120.dp
                        ),
                ) {
                    Text(
                        text = "Foto Produk",
                        style = Typo.body,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        if (imageUri !== Uri.EMPTY) {
                            Box(
                                modifier = Modifier
                                    .padding(end = 12.dp)
                                    .clip(
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                    .size(72.dp)
                            ) {
                                AsyncImage(
                                    model = imageUri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                )
                            }
                        }
                        Icon(
                            imageVector = if (imageUri === Uri.EMPTY) Icons.Rounded.Add else Icons.Rounded.Refresh,
                            contentDescription = null,
                            tint = Color.gray(),
                            modifier = Modifier
                                .clickable(
                                    indication = rememberRipple(),
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    isModalOpen = true
                                }
                                .drawBehind {
                                    drawRoundRect(
                                        color = Color.gray(),
                                        style = Stroke(
                                            width = 3f,
                                            pathEffect = PathEffect
                                                .dashPathEffect(
                                                    floatArrayOf(10f, 10f),
                                                    0f
                                                )
                                        ),
                                        cornerRadius = CornerRadius(4.dp.toPx())
                                    )
                                }
                                .padding(20.dp)
                                .size(32.dp)
                        )
                    }
                    Text(
                        text = "Informasi Produk",
                        style = Typo.body,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    TextField(
                        label = "Nama Produk",
                        control = form,
                        name = "name",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                    )
                    TextField(
                        label = "Deskripsi Produk",
                        control = form,
                        name = "description",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                    )
                    TextField(
                        label = "Stok",
                        control = form,
                        name = "stock",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                    )
                    Row(
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        TextField(
                            label = "Barcode",
                            control = form,
                            name = "barcode",
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
                    Row(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TextField(
                            label = "Harga Jual",
                            control = form,
                            name = "sell_price",
                            modifier = Modifier
                                .weight(1f),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                        )
                        TextField(
                            label = "Harga Beli",
                            control = form,
                            name = "buy_price",
                            modifier = Modifier
                                .weight(1f),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                        )
                    }
                    Text(
                        text = "Kategori",
                        style = Typo.body,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row {
                            if (categoryName.isNotEmpty()) {
                                Icon(
                                    Icons.Rounded.Label,
                                    contentDescription = null,
                                    tint = Color.gray(),
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                )
                            }
                            Text(
                                text = categoryName.ifEmpty { "Tidak ada" },
                                style = Typo.body,
                                color = if (categoryName.isNotEmpty()) Color.black() else Color.gray(),
                            )
                        }
                        Row(
                            modifier = Modifier
                                .clip(
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .clickable(
                                    indication = rememberRipple(),
                                    interactionSource = remember { MutableInteractionSource() }
                                ) {
                                    isCategoryOpen = true
                                }
                                .border(
                                    width = 1.dp,
                                    color = Color.blue(),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Pilih",
                                style = Typo.body,
                                color = Color.blue(),
                            )
                            Icon(
                                Icons.Rounded.ChevronRight,
                                contentDescription = null,
                                tint = Color.blue(),
                            )
                        }
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            form.handleSubmit {
                                val scope = CoroutineScope(Dispatchers.IO)

                                scope.launch {
                                    val categoryId =
                                        it["category_id"]?.value.toString().ifEmpty { null }
                                    val description =
                                        it["description"]?.value.toString().ifEmpty { null }
                                    val barcode = it["barcode"]?.value.toString().ifEmpty { null }

                                    val productBody = ProductViewModel
                                        .UpdateBody(
                                            name = it["name"]?.value.toString(),
                                            description = description,
                                            stock = it["stock"]?.value.toString().toInt(),
                                            barcode = barcode,
                                            sellPrice = it["sell_price"]?.value.toString()
                                                .toDouble(),
                                            buyPrice = it["buy_price"]?.value.toString().toDouble(),
                                            categoryId = categoryId,
                                            pictureUri = imageUri,
                                            pictureId = it["picture_id"]?.value.toString().ifEmpty { null }
                                        )

                                    productViewModel.update(productId, productBody) {
                                        onSuccess {
                                            Toast
                                                .makeText(
                                                    application.applicationContext,
                                                    "Berhasil menambahkan produk",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                            navController.popBackStack()
                                        }
                                        onError {
                                            Toast
                                                .makeText(
                                                    application.applicationContext,
                                                    "Gagal menambahkan produk",
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Simpan",
                            color = Color.White,
                            style = Typo.body,
                        )
                    }
                    Text(
                        text = "Hapus",
                        style = Typo.body,
                        color = Color.red(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isAlertOpen = true
                            }
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
        CategoryPickerScreen(
            isOpen = isCategoryOpen,
            onBack = remember {
                {
                    isCategoryOpen = false
                }
            },
            navController = navController,
            application = application,
            onCategorySelected = remember {
                {
                    categoryField.value = it.categoryId
                    categoryName = it.name
                    isCategoryOpen = false
                }
            },
            categoryId = categoryField.value,
        )
    }
}