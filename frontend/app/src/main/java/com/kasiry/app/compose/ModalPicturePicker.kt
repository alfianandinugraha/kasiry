package com.kasiry.app.compose

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.gray
import com.kasiry.app.utils.launcher.rememberGetContent
import com.kasiry.app.utils.rememberCameraLauncher
import com.kasiry.app.utils.rememberPermissionRequest

@Composable
fun ModalPicturePicker(
    onClose: () -> Unit,
    onFind: (Uri) -> Unit
) {
    val context = LocalContext.current
    val permission = rememberPermissionRequest("android.permission.CAMERA")
    val getContentLauncher = rememberGetContent("image/*")
    val cameraLauncher = rememberCameraLauncher(
        context = context,
        onResult = {
            if (it !== Uri.EMPTY) {
                onFind(it)
            }
        }
    )

    ModalBottom(
        title = "Pilih Gambar",
        onClose = onClose
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
                                    context,
                                    "Izin kamera tidak diberikan",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
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
                            onFind(it)
                        }
                    }
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