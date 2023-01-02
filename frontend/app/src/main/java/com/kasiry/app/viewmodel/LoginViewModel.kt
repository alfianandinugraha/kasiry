package com.kasiry.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Login
import com.kasiry.app.models.remote.AuthBody
import com.kasiry.app.utils.http.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class LoginViewModel: ViewModel() {
    private val _login = MutableStateFlow<HttpState<Login, Map<*, *>>?>(null)
    val login = _login.asStateFlow()

    companion object {
        val client = OkHttpClient.Builder().build()
    }

    fun login(
        body: AuthBody.Login,
        callback: HttpCallback<Login, Map<*, *>>.() -> Unit,
    ): Job {
        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/login",
                body
            )
            .build()

        return viewModelScope.launch {
            HttpRequest(
                state = _login,
                call = client.newCall(request),
                callback = callback,
            ).execute()
        }
    }
}