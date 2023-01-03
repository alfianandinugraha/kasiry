package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.repositories.AuthRepository
import com.kasiry.app.utils.datastore.accessToken
import com.kasiry.app.utils.http.HttpCallback
import com.kasiry.app.utils.http.HttpState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application,
    private val authRepository: AuthRepository
): AndroidViewModel(application) {

    private val _logout = MutableStateFlow<HttpState<Any>?>(null)
    val logout = _logout.asStateFlow()

    fun logout(
        callback: HttpCallback<Any>.() -> Unit,
    ): Job {
        _logout.value = HttpState.Loading()
        val context = getApplication<Application>().applicationContext

        return viewModelScope.launch {
            val response = authRepository.logout()
            _logout.value = response

            context.accessToken = null

            callback(HttpCallback(response))
        }
    }
}