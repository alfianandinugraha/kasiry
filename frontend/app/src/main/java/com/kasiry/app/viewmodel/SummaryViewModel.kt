package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Summary
import com.kasiry.app.repositories.SummaryRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class SummaryViewModel(
    application: Application,
    private val summaryRepository: SummaryRepository
): AndroidViewModel(application) {
    private val _summaryState = MutableStateFlow<HttpState<Summary>?>(null)
    val summaryState = _summaryState.asStateFlow()

    suspend fun get(
        callback: HttpCallback<Summary>.() -> Unit = {}
    ): HttpState<Summary> {
        _summaryState.value = HttpState.Loading()

        return withContext(viewModelScope.coroutineContext) {
            val response = summaryRepository.get()
            _summaryState.value = response

            callback(HttpCallback(response))

            return@withContext response
        }
    }
}