package com.kasiry.app.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Product
import com.kasiry.app.repositories.AssetRepository
import com.kasiry.app.repositories.ProductRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class ProductViewModel(
    application: Application,
    private val productRepository: ProductRepository,
    private val assetRepository: AssetRepository
): AndroidViewModel(application) {
    private val _storeState = MutableStateFlow<HttpState<Product>?>(null)
    val storeState = _storeState.asStateFlow()

    data class StoreBody(
        val name: String,
        val description: String?,
        val weight: String,
        val buyPrice: Double,
        val sellPrice: Double,
        val stock: Double,
        val barcode: String?,
        val categoryId: String?,
        val pictureUri: Uri? = null,
    )

    suspend fun store(
        body: StoreBody,
        callback: (HttpCallback<Product>.() -> Unit)? = null
    ): HttpState<Product> {
        val context = getApplication<Application>().applicationContext
        _storeState.value = HttpState.Loading()

        return withContext(viewModelScope.coroutineContext) {
            var pictureId: String? = null;
            if (body.pictureUri !== Uri.EMPTY && body.pictureUri !== null) {
                val stream = context.contentResolver.openInputStream(body.pictureUri)
                val mimeType = context.contentResolver.getType(body.pictureUri)

                if (stream !== null && mimeType !== null) {
                    val (_, type) = mimeType.split("/")

                    val assetResponse = assetRepository.upload(
                        AssetRepository.UploadBody(
                            fileName = "test.$type",
                            file = stream,
                        )
                    )

                    if (assetResponse is HttpState.Error) {
                        return@withContext HttpState.Error(
                            message = "Gagal mengupload gambar"
                        )
                    }

                    if (assetResponse is HttpState.Success) {
                        pictureId = assetResponse.data.assetId
                    }
                }
            }

            val response = productRepository.store(
                ProductRepository.StoreBody(
                    name = body.name,
                    description = body.description,
                    weight = body.weight,
                    buyPrice = body.buyPrice,
                    sellPrice = body.sellPrice,
                    stock = body.stock,
                    barcode = body.barcode,
                    categoryId = body.categoryId,
                    pictureId = pictureId,
                )
            )
            _storeState.value = response

            callback?.invoke(HttpCallback(response))

            return@withContext response
        }
    }

    private val _listState = MutableStateFlow<HttpState<List<Product>>?>(null)
    private val _filterListState = MutableStateFlow<HttpState<List<Product>>?>(null)
    val listState = _listState.asStateFlow()
    val filterListState = _listState.asStateFlow()

    suspend fun getAll(
        query: String? = null,
        callback: (HttpCallback<List<Product>>.() -> Unit)? = null
    ): HttpState<List<Product>> {
        _listState.value = HttpState.Loading()

        return withContext(viewModelScope.coroutineContext) {
            val response = productRepository.getAll(query)
            _listState.value = response

            callback?.invoke(HttpCallback(response))

            return@withContext response
        }
    }

    fun filter(query: String): Unit {
        when (val list = _listState.value) {
            is HttpState.Success -> {
                val products = list.data
                val filtered = products.filter {
                    it.name.contains(query, true) || it.barcode?.contains(query, true) == true
                }

                _filterListState.value = HttpState.Success(filtered)
            }
            else -> {}
        }
    }

    private val _deleteState = MutableStateFlow<HttpState<Product>?>(null)
    val deleteState = _deleteState.asStateFlow()

    suspend fun delete(
        productId: String,
        callback: (HttpCallback<Product>.() -> Unit)? = null
    ): HttpState<Product> {
        _deleteState.value = HttpState.Loading()

        return withContext(viewModelScope.coroutineContext) {
            val response = productRepository.delete(productId)
            _deleteState.value = response

            callback?.invoke(HttpCallback(response))

            return@withContext response
        }
    }

    private val _updateState = MutableStateFlow<HttpState<Product>?>(null)
    val updateState = _updateState.asStateFlow()

    suspend fun update(
        productId: String,
        body: ProductRepository.UpdateBody,
        callback: (HttpCallback<Product>.() -> Unit)? = null
    ): HttpState<Product> {
        _updateState.value = HttpState.Loading()

        return withContext(viewModelScope.coroutineContext) {
            val response = productRepository.update(productId, body)
            _updateState.value = response

            callback?.invoke(HttpCallback(response))

            return@withContext response
        }
    }

    suspend fun get(
        productId: String,
        callback: (HttpCallback<Product>.() -> Unit)? = null
    ): HttpState<Product> {
        return withContext(viewModelScope.coroutineContext) {
            val response = productRepository.get(productId)
            callback?.invoke(HttpCallback(response))

            return@withContext response
        }
    }
}