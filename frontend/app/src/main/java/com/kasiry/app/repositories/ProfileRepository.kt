package com.kasiry.app.repositories

import android.content.Context
import com.kasiry.app.models.data.Profile
import com.kasiry.app.utils.http.*
import okhttp3.OkHttpClient

class ProfileRepository(context: Context) {
    private val client = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    suspend fun get(): HttpState<Profile> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/profile",
            )
            .build()

        return HttpRequest
            .create<Profile>(
                call = client.newCall(request),
            )
            .execute()
    }

    private val updateClient = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    suspend fun update(profile: Profile): HttpState<Profile> {
        val request = ApiRequest
            .json(
                HttpMethod.PUT,
                "/profile",
                profile
            )
            .build()

        return HttpRequest
            .create<Profile>(
                call = updateClient.newCall(request),
            )
            .execute()
    }
}