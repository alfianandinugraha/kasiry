package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Employee
import com.kasiry.app.repositories.EmployeeRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployeeCreateViewModel(
    application: Application,
    val employeeRepository: EmployeeRepository
): AndroidViewModel(application) {
    private val _update = MutableStateFlow<HttpState<Employee>?>(null)
    val update = _update.asStateFlow()

    fun update (
        employee: EmployeeRepository.UpdateBody,
        callback: HttpCallback<Employee>.() -> Unit
    ) {
        _update.value = HttpState.Loading()

        viewModelScope.launch {
            val response = employeeRepository.update(employee)
            _update.value = response

            callback(HttpCallback(response))
        }
    }
}