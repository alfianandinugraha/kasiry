package com.kasiry.app.repositories

import android.content.Context
import com.kasiry.app.models.data.Summary
import com.kasiry.app.utils.http.*
import okhttp3.OkHttpClient

class SummaryRepository(
    context: Context
) {
    private val client = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    suspend fun get(): HttpState<Summary> {
        val request = ApiRequest
            .json(HttpMethod.GET, "/summary")
            .build()

        return HttpRequest
            .create<Summary>(
                call = client.newCall(request)
            )
            .execute()
    }
}