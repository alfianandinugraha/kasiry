package com.kasiry.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun rememberCameraLauncher(context: Context, onResult: (Uri) -> Unit): CameraLauncher {
    var file by remember {
        mutableStateOf(Uri.EMPTY)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = {
            if (Activity.RESULT_OK == it.resultCode) {
                onResult(file)
            }
        }
    )

    fun launch() {
        val timestamp = System.currentTimeMillis() / 1000
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val fileTemp = File.createTempFile("kasiry_$timestamp", ".jpg", dir)
        val fileTempUri = FileProvider.getUriForFile(
            context,
            "com.kasiry.app.provider",
            fileTemp
        )

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        intent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            fileTempUri
        )

        file = fileTempUri

        launcher.launch(intent)
    }

    return object : CameraLauncher {
        override fun launch() {
            launch()
        }
    }
}

interface CameraLauncher {
    fun launch()
}