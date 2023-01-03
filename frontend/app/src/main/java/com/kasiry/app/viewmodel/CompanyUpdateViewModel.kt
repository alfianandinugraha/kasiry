package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Company
import com.kasiry.app.repositories.CompanyRepository
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CompanyUpdateViewModel(
    application: Application,
    private val companyRepository: CompanyRepository
): AndroidViewModel(application) {
    private val _company = MutableStateFlow<HttpState<Company>?>(null)
    val company = _company.asStateFlow()

    fun update(
        company: Company,
        callback: HttpCallback<Company>.() -> Unit
    ): Job {
        _company.value = HttpState.Loading()

        return viewModelScope.launch {
            val response = companyRepository.update(company)
            _company.value = response

            callback(HttpCallback(response))
        }
    }
}