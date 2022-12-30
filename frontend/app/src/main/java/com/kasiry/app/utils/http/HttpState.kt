package com.kasiry.app.utils.http

sealed class HttpState<out S, out E>() {
    class Success<S>(
        val data: S,
        val message: String? = "",
        val statusCode: Int = 200
    ) : HttpState<S, Nothing>()

    class Error<E>(
        val data: E? = null,
        val message: String? = "",
        val statusCode: Int = 400
    ) : HttpState<Nothing, E>()

    class Loading<S, E> : HttpState<S, E>()
}
