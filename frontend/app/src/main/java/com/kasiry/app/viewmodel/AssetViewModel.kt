package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Asset
import com.kasiry.app.repositories.AssetRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class AssetViewModel(
    application: Application,
    private val assetRepository: AssetRepository
): AndroidViewModel(application) {
    private val _upload = MutableStateFlow<HttpState<Asset>?>(null)
    val upload = _upload.asStateFlow()

    suspend fun upload(
        body: AssetRepository.UploadBody,
        callback: (HttpCallback<Asset>.() -> Unit)? = null
    ): HttpState<Asset> {
        _upload.value = HttpState.Loading()

        return withContext(viewModelScope.coroutineContext) {
            val response = assetRepository.upload(body)
            _upload.value = response

            callback?.invoke(HttpCallback(response))

            return@withContext response
        }
    }
}