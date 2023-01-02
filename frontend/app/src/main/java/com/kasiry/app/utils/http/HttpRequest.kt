package com.kasiry.app.utils.http

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import okhttp3.*

class HttpRequest <S, E>(
    val state: MutableStateFlow<HttpState<S, E>?>,
    val callback: HttpCallback<S, E>.() -> Unit,
    val call: Call,
) {
    suspend fun execute(): HttpState<S, E> = withContext(Dispatchers.IO) {
        state.value = HttpState.Loading()

        try {
            val response = call.execute()
            val body = response.body?.string()

            if (response.isSuccessful) {
                val (data, message) = Gson()
                    .fromJson<HttpResponse<S>>(
                        body, object : TypeToken<HttpResponse<S>>() {}.type
                    )

                val value = HttpState.Success(
                    data = data,
                    message = message,
                    statusCode = response.code
                )

                state.value = value
                withContext(Dispatchers.Main) { callback(HttpCallback(value)) }

                value
            } else {
                val (data, message) = Gson()
                    .fromJson<HttpResponse<E>>(
                        body, object : TypeToken<HttpResponse<E>>() {}.type
                    )

                val value = HttpState.Error(
                    data = data,
                    message = message,
                    statusCode = response.code
                )

                state.value = value
                withContext(Dispatchers.Main) { callback(HttpCallback(value)) }

                value
            }
        } catch (e: Exception) {
            val value = HttpState.Error(
                message = e.message,
                data = null,
                statusCode = 500
            )

            state.value = value
            withContext(Dispatchers.Main) { callback(HttpCallback(value)) }

            value
        }
    }
}
