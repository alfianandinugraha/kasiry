package com.kasiry.app.utils.http

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kasiry.app.models.data.Login
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import okhttp3.*
import java.lang.reflect.Type

class HttpRequest <S, E>(
    val state: MutableStateFlow<HttpState<S, E>?>,
    val callback: (HttpCallback<S, E>.() -> Unit)? = null,
    val call: Call,
    val success: Type,
    val error: Type,
) {
    companion object {
        inline fun <reified S, reified E> create(
            state: MutableStateFlow<HttpState<S, E>?>,
            noinline callback: (HttpCallback<S, E>.() -> Unit)? = null,
            call: Call,
        ) = HttpRequest(
            state = state,
            callback = callback,
            call = call,
            success = object : TypeToken<HttpResponse<S>>() {}.type,
            error = object : TypeToken<HttpResponse<E>>() {}.type
        )
    }

    suspend fun execute(): HttpState<S, E> = withContext(Dispatchers.IO) {
        state.value = HttpState.Loading()

        try {
            val response = call.execute()
            val body = response.body?.string()

            if (response.isSuccessful) {
                val result = Gson()
                    .fromJson<HttpResponse<S>>(body, success)

                val value = HttpState.Success(
                    data = result.data,
                    message = result.message,
                    statusCode = response.code
                )

                state.value = value.copy()
                withContext(Dispatchers.Main) { callback?.let { it(HttpCallback(value.copy())) } }

                value
            } else {
                val (data, message, errors) = Gson()
                    .fromJson<HttpResponse<E>>(
                        body, error
                    )

                val value = HttpState.Error(
                    data = data,
                    message = message,
                    statusCode = response.code,
                    errors = errors
                )

                state.value = value.copy()
                withContext(Dispatchers.Main) { callback?.let { it(HttpCallback(value.copy())) } }

                value
            }
        } catch (e: Exception) {
            val value = HttpState.Error(
                message = e.message,
                data = null,
                statusCode = 500
            )

            state.value = value.copy()
            withContext(Dispatchers.Main) { callback?.let { it(HttpCallback(value.copy())) } }

            value
        }
    }
}
