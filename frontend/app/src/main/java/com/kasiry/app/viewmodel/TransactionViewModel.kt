package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Transaction
import com.kasiry.app.repositories.TransactionRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class TransactionViewModel(
    application: Application,
    private val transactionRepository: TransactionRepository
): AndroidViewModel(application) {
    private val _listState = MutableStateFlow<HttpState<List<Transaction>>?>(null)
    val listState = _listState.asStateFlow()

    suspend fun getAll(
        callback: HttpCallback<List<Transaction>>.() -> Unit = {}
    ): HttpState<List<Transaction>> {
        _listState.value = HttpState.Loading()

        return withContext(viewModelScope.coroutineContext) {
            val response = transactionRepository.getAll()
            _listState.value = response

            callback.invoke(
                HttpCallback(
                    response
                )
            )

            return@withContext response
        }
    }

    private val _storeState = MutableStateFlow<HttpState<Transaction>?>(null)
    val storeState = _storeState.asStateFlow()

    suspend fun store(
        body: TransactionRepository.StoreBody,
        callback: HttpCallback<Transaction>.() -> Unit = {}
    ): HttpState<Transaction> {
        _storeState.value = HttpState.Loading()

        return withContext(viewModelScope.coroutineContext) {
            val response = transactionRepository.store(body)
            _storeState.value = response

            callback.invoke(
                HttpCallback(
                    response
                )
            )

            return@withContext response
        }
    }

    suspend fun detail(
        transactionId: String,
        callback: HttpCallback<Transaction>.() -> Unit = {}
    ): HttpState<Transaction> {
        return withContext(viewModelScope.coroutineContext) {
            val response = transactionRepository.detail(transactionId)
            callback(HttpCallback(response))
            return@withContext response
        }
    }
}