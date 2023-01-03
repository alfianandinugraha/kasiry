package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Login
import com.kasiry.app.models.remote.AuthBody
import com.kasiry.app.repositories.AuthRepository
import com.kasiry.app.utils.datastore.accessToken
import com.kasiry.app.utils.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient

class LoginViewModel(
    application: Application,
    private val client: OkHttpClient = OkHttpClient
        .Builder()
        .build(),
    private val authRepository: AuthRepository = AuthRepository(client),
): AndroidViewModel(application) {
    private val _login = MutableStateFlow<HttpState<Login>?>(null)
    val login = _login.asStateFlow()

    fun login(
        body: AuthRepository.LoginBody,
        callback: HttpCallback<Login>.() -> Unit,
    ): Job {
        _login.value = HttpState.Loading()

        return viewModelScope.launch(Dispatchers.Main) {
            val result = authRepository.login(body)
            _login.value = result

            val context = getApplication<Application>().applicationContext

            if (result is HttpState.Success) {
                context.accessToken = result.data.token
            }

            if (result is HttpState.Error) {
                context.accessToken = null
            }

            callback(HttpCallback(result))
        }
    }
}