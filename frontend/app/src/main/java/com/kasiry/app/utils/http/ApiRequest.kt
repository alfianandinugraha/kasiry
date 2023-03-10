package com.kasiry.app.utils.http

import android.net.Uri
import com.google.gson.Gson
import com.kasiry.app.BuildConfig
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import java.net.URI
import java.net.URL

class ApiRequest {
    companion object {
        private const val API_BASE_URL = BuildConfig.API_BASE_URL

        private fun headers(headers: Map<String, String>): Headers {
            val builder = Headers.Builder()

            headers.forEach { (key, value) ->
                builder.add(key, value)
            }

            return builder.build()
        }

        private fun mapMethod(method: HttpMethod): String {
            when (method) {
                HttpMethod.GET -> {
                    return "GET"
                }
                HttpMethod.POST -> {
                    return "POST"
                }
                HttpMethod.PUT -> {
                    return "PUT"
                }
                HttpMethod.DELETE -> {
                    return "DELETE"
                }
                else -> {
                    return "GET"
                }
            }
        }

        fun json(
            method: HttpMethod,
            path: String,
            body: Any? = null,
            headers: Map<String, String> = mapOf(),
            params: Map<String, Any?> = mapOf()
        ): Request.Builder {
            val uri = Uri.parse("$API_BASE_URL$path")
                .buildUpon()

            params.forEach { (key, value) ->
                if (value != null) {
                    uri.appendQueryParameter(key, value.toString())
                }
            }

            uri.build()

            return Request.Builder()
                .url(uri.toString())
                .headers(headers(headers.toMutableMap().let {
                    it["Accept"] = "application/json"
                    it
                }))
                .method(
                    mapMethod(method),
                    if (body != null) {
                        Gson().toJson(body).toRequestBody("application/json".toMediaTypeOrNull())
                    } else {
                        if (method == HttpMethod.GET) {
                            null
                        } else {
                            EMPTY_REQUEST
                        }
                    }
                )
        }

        fun formData(
            method: HttpMethod,
            path: String,
            body: MultipartBody? = null,
            headers: Map<String, String> = mapOf()
        ): Request.Builder {
            val url = "${API_BASE_URL}${path}"
            return Request.Builder()
                .url(url)
                .headers(headers(headers.toMutableMap().let {
                    it["Accept"] = "application/json"
                    it
                }))
                .method(
                    mapMethod(method),
                    body
                        ?: if (method == HttpMethod.GET) {
                            null
                        } else {
                            EMPTY_REQUEST
                        }
                )
        }
    }
}