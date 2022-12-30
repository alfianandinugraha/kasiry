package com.kasiry.app.utils.http

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import okhttp3.Call
import java.io.IOException

class HttpRequest <S, E>(
    val state: MutableStateFlow<HttpState<S, E>?>,
    val callback: HttpCallback<S, E>.() -> Unit,
    val call: Call
) {
    suspend fun execute(): HttpState<S, E>? {
        state.value = HttpState.Loading()

        try {
            val response = call.execute()
            if (response.isSuccessful) {
                val data = Gson().fromJson<S>(response.body?.string(), object : TypeToken<S>() {}.type)
                val value = HttpState.Success(
                    data = data,
                    message = response.message,
                    statusCode = response.code
                )

                state.value = value
                withContext(Dispatchers.Main) {
                    callback(HttpCallback(value))
                }

                return value
            } else {
                val data = Gson().fromJson<E>(response.body?.string(), Map::class.java)
                val value = HttpState.Error(
                    data = data,
                    message = response.message,
                    statusCode = response.code
                )

                state.value = value
                withContext(Dispatchers.Main) {
                    callback(HttpCallback(value))
                }

                return value
            }
        } catch (e: IOException) {
            val value = HttpState.Error(
                message = e.message,
                data = null,
                statusCode = 500
            )

            state.value = value
            withContext(Dispatchers.Main) {
                callback(HttpCallback(value))
            }

            return value
        }
    }
}
