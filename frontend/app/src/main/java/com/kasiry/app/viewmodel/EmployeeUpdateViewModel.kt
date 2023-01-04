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

class EmployeeUpdateViewModel(
    application: Application,
    val employeeRepository: EmployeeRepository
): AndroidViewModel(application) {
    private val _employee = MutableStateFlow<HttpState<Employee>?>(null)
    val employee = _employee.asStateFlow()

    private val _update = MutableStateFlow<HttpState<Employee>?>(null)
    val update = _update.asStateFlow()

    private val _delete = MutableStateFlow<HttpState<Any>?>(null)
    val delete = _delete.asStateFlow()

    fun get (
        userId: String,
        callback: HttpCallback<Employee>.() -> Unit
    ) {
        _employee.value = HttpState.Loading()

        viewModelScope.launch {
            val response = employeeRepository.get(userId)
            _employee.value = response

            callback(HttpCallback(response))
        }
    }

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

    fun delete (
        userId: String,
        callback: HttpCallback<Any>.() -> Unit
    ) {
        _delete.value = HttpState.Loading()

        viewModelScope.launch {
            val response = employeeRepository.delete(userId)
            _delete.value = response

            callback(HttpCallback(response))
        }
    }
}