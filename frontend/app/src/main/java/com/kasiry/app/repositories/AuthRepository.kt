package com.kasiry.app.repositories

import com.kasiry.app.models.data.Login
import com.kasiry.app.utils.http.ApiRequest
import com.kasiry.app.utils.http.HttpMethod
import com.kasiry.app.utils.http.HttpRequest
import com.kasiry.app.utils.http.HttpState
import okhttp3.OkHttpClient

class AuthRepository(private val client: OkHttpClient) {
    data class LoginBody (
        val email: String,
        val password: String,
    )

    data class RegisterBody (
        val name: String,
        val email: String,
        val password: String,
    )

    suspend fun login(body: LoginBody): HttpState<Login> {
        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/login",
                body
            )
            .build()

        return HttpRequest.create<Login>(
            call = client.newCall(request),
        ).execute()
    }

    suspend fun register(body: RegisterBody): HttpState<Login> {
        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/register",
                body
            )
            .build()

        return HttpRequest.create<Login>(
            call = client.newCall(request),
        ).execute()
    }

    suspend fun logout(): HttpState<Login> {
        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/logout",
            )
            .build()

        return HttpRequest.create<Login>(
            call = client.newCall(request),
        ).execute()
    }
}