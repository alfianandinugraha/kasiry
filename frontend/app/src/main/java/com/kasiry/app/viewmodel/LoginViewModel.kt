package com.kasiry.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Login
import com.kasiry.app.models.data.Profile
import com.kasiry.app.repositories.AuthRepository
import com.kasiry.app.repositories.ProfileRepository
import com.kasiry.app.utils.datastore.accessToken
import com.kasiry.app.utils.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient

class LoginViewModel(
    application: Application,
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
): AndroidViewModel(application) {
    private val _login = MutableStateFlow<HttpState<Login>?>(null)
    val login = _login.asStateFlow()

    fun login(
        body: AuthRepository.LoginBody,
        callback: HttpCallback<Profile>.() -> Unit,
    ): Job {
        _login.value = HttpState.Loading()

        return viewModelScope.launch(Dispatchers.Main) {
            val loginResponse = authRepository.login(body)
            _login.value = loginResponse

            val context = getApplication<Application>().applicationContext

            if (loginResponse is HttpState.Success) {
                context.accessToken = loginResponse.data.token
            }

            if (loginResponse is HttpState.Error) {
                context.accessToken = null
            }

            val profileResponse = profileRepository.get()

            callback(HttpCallback(profileResponse))
        }
    }
}