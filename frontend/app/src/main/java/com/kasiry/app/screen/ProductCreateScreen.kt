package com.kasiry.app.screen

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.kasiry.app.compose.*
import com.kasiry.app.rules.required
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.gray
import com.kasiry.app.utils.*
import com.kasiry.app.utils.launcher.rememberGetContent

@Composable
fun ProductCreateScreen(
    navController: NavController,
    application: Application
) {
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
                "weight" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
            )
        )
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

    val cameraLauncher = rememberCameraLauncher(
        context = application.applicationContext,
        onResult = {
            if (it !== Uri.EMPTY) {
                imageUri = it
            }
            Log.d("Camera", "onResult: $it")
        }
    )
    val permission = rememberPermissionRequest("android.permission.CAMERA")
    val getContentLauncher = rememberGetContent("image/*")

    if (isCameraOpen) {
        Box {
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
        if (isModalOpen) {
            ModalBottom(
                title = "Pilih Gambar",
                onClose = {
                    isModalOpen = false
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) {
                            permission.launch {
                                onGranted {
                                    cameraLauncher.launch()
                                }
                                onDenied {
                                    Toast
                                        .makeText(
                                            application.applicationContext,
                                            "Izin kamera tidak diberikan",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()
                                }
                            }
                            isModalOpen = false
                        }
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Rounded.Camera,
                        contentDescription = null,
                        tint = Color.gray(),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(28.dp)
                    )
                    Text(
                        text = "Ambil Foto",
                        style = Typo.body,
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(8.dp)
                        )
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(bounded = false)
                        ) {
                            getContentLauncher.launch {
                                onSuccess {
                                    imageUri = it
                                }
                            }
                            isModalOpen = false
                        }
                        .padding(8.dp)
                ) {
                    Icon(
                        Icons.Rounded.Image,
                        contentDescription = null,
                        tint = Color.gray(),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(28.dp)
                    )
                    Text(
                        text = "Pilih dari Galeri",
                        style = Typo.body,
                    )
                }
            }
        }
        Layout(
            topbar = {
                TopBar(
                    title = "Tambah Produk",
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
                    Row(
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .height(IntrinsicSize.Min),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        TextField(
                            label = "Stok",
                            control = form,
                            name = "stock",
                            modifier = Modifier
                                .weight(1f),
                        )
                        TextField(
                            label = "Satuan",
                            control = form,
                            name = "weight",
                            modifier = Modifier
                                .weight(1f),
                        )
                    }
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
                        )
                        TextField(
                            label = "Harga Beli",
                            control = form,
                            name = "buy_price",
                            modifier = Modifier
                                .weight(1f),
                        )
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            form.handleSubmit {

                            }
                        }
                    ) {
                        Text(
                            text = "Tambah",
                            color = Color.White,
                            style = Typo.body,
                        )
                    }
                }
            }
        }
    }

}