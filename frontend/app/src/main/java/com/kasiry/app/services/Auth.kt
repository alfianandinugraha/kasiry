package com.kasiry.app.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kasiry.app.utils.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*

class AuthService: ViewModel() {
    companion object {
        val client = OkHttpClient.Builder().build()
    }

    class Login: ViewModel() {
        private val _login = MutableStateFlow<HttpState<Login, Map<*, *>>?>(null)
        val login = _login.asStateFlow()

        data class Body (
            val email: String,
            val password: String
        )

        fun createLogin(
            body: Body,
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
                withContext(Dispatchers.IO) {
                    HttpRequest(
                        state = _login,
                        call = client.newCall(request),
                        callback = callback,
                    ).execute()
                }
            }
        }
    }

    class Register: ViewModel() {
        private val _state = MutableStateFlow<HttpState<Register, Map<*, *>>?>(null)
        val state = _state.asStateFlow()

        data class Body (
            val name: String,
            val email: String,
            val password: String,
            val password_confirmation: String,
        )

        fun register(
            body: Body,
            callback: HttpCallback<Register, Map<*, *>>.() -> Unit,
        ): Job {
            val request = ApiRequest
                .json(
                    HttpMethod.POST,
                    "/register",
                    body
                )
                .build()

            return viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    HttpRequest(
                        state = _state,
                        call = client.newCall(request),
                        callback = callback,
                    ).execute()
                }
            }
        }
    }

    class Logout(): ViewModel() {
        private val _state = MutableStateFlow<HttpState<Boolean, Map<*, *>>?>(null)
        val state = _state.asStateFlow()

        suspend fun logout(
            callback: HttpCallback<Boolean, Map<*, *>>.() -> Unit,
        ): Job {
            val request = ApiRequest
                .json(
                    HttpMethod.POST,
                    "/logout",
                )
                .build()

            return viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    HttpRequest(
                        state = _state,
                        call = client.newCall(request),
                        callback = callback,
                    ).execute()
                }
            }
        }
    }
}
