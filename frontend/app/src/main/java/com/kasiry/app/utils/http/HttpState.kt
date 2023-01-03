package com.kasiry.app.utils.http

sealed class HttpState<out S>() {
    class Success<S>(
        val data: S,
        val message: String? = "",
        val statusCode: Int = 200
    ) : HttpState<S>() {
        fun copy() = Success(data, message, statusCode)

        override fun toString(): String {
            return "Success(data=$data, message=$message, statusCode=$statusCode)"
        }
    }

    class Error(
        val message: String? = "",
        val statusCode: Int = 400,
        val errors: Map<String, List<String>>? = null
    ) : HttpState<Nothing>() {
        fun copy() = Error(message, statusCode, errors)

        override fun toString(): String {
            return "Error(message=$message, statusCode=$statusCode, errors=$errors)"
        }
    }

    class Loading<S> : HttpState<S>()
}
