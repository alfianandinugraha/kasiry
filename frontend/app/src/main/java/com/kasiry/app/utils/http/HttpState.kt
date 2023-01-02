package com.kasiry.app.utils.http

sealed class HttpState<out S, out E>() {
    class Success<S>(
        val data: S,
        val message: String? = "",
        val statusCode: Int = 200
    ) : HttpState<S, Nothing>() {
        fun copy() = Success(data, message, statusCode)
    }

    class Error<E>(
        val data: E? = null,
        val message: String? = "",
        val statusCode: Int = 400,
        val errors: Map<String, List<String>>? = null
    ) : HttpState<Nothing, E>() {
        fun copy() = Error(data, message, statusCode, errors)
    }

    class Loading<S, E> : HttpState<S, E>()
}
