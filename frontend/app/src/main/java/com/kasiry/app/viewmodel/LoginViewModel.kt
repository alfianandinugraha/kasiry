package com.kasiry.app.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.models.data.Login
import com.kasiry.app.models.remote.AuthBody
import com.kasiry.app.utils.datastore.accessToken
import com.kasiry.app.utils.http.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient

class LoginViewModel(val context: Context): ViewModel() {
    private val _login = MutableStateFlow<HttpState<Login, Map<*, *>>?>(null)
    val login = _login.asStateFlow()

    fun login(
        body: AuthBody.Login,
        callback: HttpCallback<Login, Map<*, *>>.() -> Unit,
    ): Job {
        val client = OkHttpClient
            .Builder()
            .build()

        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/login",
                body
            )
            .build()

        return viewModelScope.launch(Dispatchers.Main) {
            val result = HttpRequest.create(
                state = _login,
                call = client.newCall(request),
            ).execute()

            if (result is HttpState.Success) {
                context.accessToken = result.data.token
                callback(HttpCallback(result))
            }

            if (result is HttpState.Error) {
                context.accessToken = null
                callback(HttpCallback(result))
            }
        }
    }
}