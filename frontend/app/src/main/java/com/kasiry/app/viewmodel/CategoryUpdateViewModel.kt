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

    private val _delete = MutableStateFlow<HttpState<Any>?>(null)
    val delete = _delete.asStateFlow()

    fun get(
        categoryId: String,
        callback: HttpCallback<Category>.() -> Unit
    ) {
        _category.value = HttpState.Loading()

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
        _update.value = HttpState.Loading()

        viewModelScope.launch {
            val response = categoryRepository.update(category)
            _update.value = response

            callback(HttpCallback(response))
        }
    }

    fun delete(
        categoryId: String,
        callback: HttpCallback<Any>.() -> Unit
    ) {
        _delete.value = HttpState.Loading()

        viewModelScope.launch {
            val response = categoryRepository.delete(categoryId)
            _delete.value = response

            callback(HttpCallback(response))
        }
    }
}