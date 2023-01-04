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

class CategoryViewModel(
    application: Application,
    private val categoryRepository: CategoryRepository
) : AndroidViewModel(application) {
    private val _categories = MutableStateFlow<HttpState<List<Category>>?>(null)
    val categories = _categories.asStateFlow()

    fun getAll(
        callback: HttpCallback<List<Category>>.() -> Unit
    ) {
        viewModelScope.launch {
            val response = categoryRepository.getAll()
            _categories.value = response

            callback(HttpCallback(response))
        }
    }
}