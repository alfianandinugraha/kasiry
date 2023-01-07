package com.kasiry.app.screen

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraView(
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    onFound: (String) -> Unit,
    onError: (Exception) -> Unit,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = modifier) {
        AndroidView(
            factory = { context ->
                val previewView = PreviewView(context).apply {
                    this.scaleType = scaleType
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                val previewUseCase = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val analysisUseCase = ImageAnalysis.Builder()
                    .build()

                analysisUseCase.setAnalyzer(
                    context.executor
                ) {
                    processImageProxy(
                        barcodeScanner = BarcodeScanning.getClient(),
                        it,
                        onFound = { it ->
                            onFound(it)
                            Log.d("CameraView", "onFound: $it")
                        }
                    )
                }

                coroutineScope.launch {
                    val cameraProvider = context.getCameraProvider()
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, previewUseCase, analysisUseCase
                        )
                    } catch (ex: Exception) {
                        onError(ex)
                        Log.e("CameraPreview", "Use case binding failed", ex)
                    }
                }

                previewView
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
                    onClose?.invoke()
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
}

@SuppressLint("UnsafeOptInUsageError")
private fun processImageProxy(
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy,
    onFound: (String) -> Unit = {},
) {
    val inputImage =
        InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)

    barcodeScanner.process(inputImage)
        .addOnSuccessListener { barcodes ->
            barcodes.forEach { it ->
                it.rawValue?.let { it ->
                    onFound(it)
                }
            }
        }
        .addOnFailureListener {

        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { provider ->
        provider.addListener({
            continuation.resume(provider.get())
        }, executor)
    }
}