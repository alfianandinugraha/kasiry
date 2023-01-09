package com.kasiry.app.utils.http

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import java.lang.reflect.Type

class HttpRequest <S>(
    val call: Call,
    val success: Type,
    val error: Type,
) {
    companion object {
        inline fun <reified S> create(call: Call) = HttpRequest<S>(
            call = call,
            success = object : TypeToken<HttpState.Success<S>>() {}.type,
            error = object : TypeToken<HttpState.Error>() {}.type
        )
    }

    suspend fun execute(): HttpState<S> = withContext(Dispatchers.IO) {
        try {
            val response = call.execute()
            val body = response.body?.string()

            Log.d("execute()", body.toString())

            if (response.isSuccessful) {
                val result = Gson()
                    .fromJson<HttpState.Success<S>>(body, success)

                val value = HttpState.Success(
                    data = result.data,
                    message = result.message,
                    statusCode = response.code
                )

                value
            } else {
                val json = Gson()
                    .fromJson<HttpState.Error>(
                        body, error
                    )

                Log.d("HttpRequest", "Error: $json")

                val value = HttpState.Error(
                    message = json.message,
                    statusCode = response.code,
                    errors = json.errors
                )
                value
            }
        } catch (e: Exception) {
            val value = HttpState.Error(
                message = e.message,
                statusCode = 500
            )

            value
        }
    }
}
