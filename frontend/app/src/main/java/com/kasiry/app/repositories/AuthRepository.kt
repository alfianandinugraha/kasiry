package com.kasiry.app.repositories

import android.content.Context
import com.kasiry.app.models.data.Login
import com.kasiry.app.utils.http.*
import okhttp3.OkHttpClient

class AuthRepository(
    context: Context,
) {
    data class LoginBody (
        val email: String,
        val password: String,
    )

    data class RegisterBody (
        val name: String,
        val email: String,
        val password: String,
    )

    private val client = OkHttpClient
        .Builder()
        .build()

    private val logoutClient = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

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
            call = logoutClient.newCall(request),
        ).execute()
    }
}