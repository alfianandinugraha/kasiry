package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Category
import com.kasiry.app.repositories.CategoryRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryCreateViewModel(
    application: Application,
    private val categoryRepository: CategoryRepository
) : AndroidViewModel(application) {
    private val _create = MutableStateFlow<HttpState<Category>?>(null)
    val create = _create.asStateFlow()

    fun create(
        category: Category,
        callback: HttpCallback<Category>.() -> Unit
    ) {
        _create.value = HttpState.Loading()

        viewModelScope.launch {
            val response = categoryRepository.create(category)
            _create.value = response

            callback(HttpCallback(response))
        }
    }
}