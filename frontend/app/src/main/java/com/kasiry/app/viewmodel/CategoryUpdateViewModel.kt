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

class CategoryUpdateViewModel(
    application: Application,
    private val categoryRepository: CategoryRepository
) : AndroidViewModel(application) {
    private val _update = MutableStateFlow<HttpState<Category>?>(null)
    val update = _update.asStateFlow()

    private val _category = MutableStateFlow<HttpState<Category>?>(null)
    val category = _category.asStateFlow()

    fun get(
        categoryId: String,
        callback: HttpCallback<Category>.() -> Unit
    ) {
        viewModelScope.launch {
            val response = categoryRepository.get(categoryId)
            _category.value = response

            callback(HttpCallback(response))
        }
    }

    fun update(
        category: Category,
        callback: HttpCallback<Category>.() -> Unit
    ) {
        viewModelScope.launch {
            val response = categoryRepository.update(category)
            _update.value = response

            callback(HttpCallback(response))
        }
    }
}