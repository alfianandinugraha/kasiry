package com.kasiry.app.utils

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberPermissionRequest(input: String): PermissionRequest {
    var fn: PermissionRequestResult.() -> Unit by remember {
        mutableStateOf({})
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        fn(
            object : PermissionRequestResult {
                override fun onGranted(callback: () -> Unit) {
                    if (it) callback()
                }

                override fun onDenied(callback: () -> Unit) {
                    if (!it) callback()
                }
            }
        )
    }

    return object : PermissionRequest {
        override fun launch(callback: PermissionRequestResult.() -> Unit) {
            fn = callback
            launcher.launch(input)
        }
    }
}

interface PermissionRequest {
    fun launch(callback: PermissionRequestResult.() -> Unit)
}

interface PermissionRequestResult {
    fun onGranted(callback: () -> Unit)
    fun onDenied(callback: () -> Unit)
}