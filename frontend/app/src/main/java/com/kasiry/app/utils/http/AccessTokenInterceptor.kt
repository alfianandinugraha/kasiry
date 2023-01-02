package com.kasiry.app.utils.http

import android.content.Context
import android.util.Log
import com.kasiry.app.utils.datastore.accessToken
import okhttp3.Interceptor
import okhttp3.Response

class AccessTokenInterceptor(private val context: Context): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        Log.d("AccessTokenInterceptor", "thread: ${Thread.currentThread().name}")
        val token = context.accessToken
        Log.d("AccessTokenInterceptor", "token: $token")

        val request = original.newBuilder()
            .header("Authorization", "Bearer $token")
            .method(original.method, original.body)
            .build()

        return chain.proceed(request)
    }
}