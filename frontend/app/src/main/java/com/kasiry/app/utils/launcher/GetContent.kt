package com.kasiry.app.utils.launcher

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*

@Composable
fun rememberGetContent(type: String): GetContentRequest {
    var fn: GetContentRequestResult.() -> Unit by remember {
        mutableStateOf({})
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        fn(
            object : GetContentRequestResult {
                override fun onSuccess(callback: (uri: Uri) -> Unit) {
                    if (it !== Uri.EMPTY && it !== null) callback(it)
                }

                override fun onError(callback: () -> Unit) {
                    if (it === Uri.EMPTY) callback()
                }
            }
        )
    }

    return object : GetContentRequest {
        override fun launch(callback: GetContentRequestResult.() -> Unit) {
            fn = callback
            launcher.launch(type)
        }
    }
}

interface GetContentRequest {
    fun launch(callback: GetContentRequestResult.() -> Unit)
}

interface GetContentRequestResult {
    fun onSuccess(callback: (uri: Uri) -> Unit)
    fun onError(callback: () -> Unit)
}