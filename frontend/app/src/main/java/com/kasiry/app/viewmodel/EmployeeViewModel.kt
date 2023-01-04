package com.kasiry.app.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Employee
import com.kasiry.app.repositories.EmployeeRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel(
    application: Application,
    val employeeRepository: EmployeeRepository
): AndroidViewModel(application) {
    private val _employees = MutableStateFlow<HttpState<List<Employee>>?>(null)
    val employees = _employees.asStateFlow()

    fun getAll(
        callback: HttpCallback<List<Employee>>.() -> Unit
    ): Job {
        _employees.value = HttpState.Loading()

        return viewModelScope.launch {
            val response = employeeRepository.getAll()
            _employees.value = response

            callback(HttpCallback(response))
        }
    }
}