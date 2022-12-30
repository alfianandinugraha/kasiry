package com.kasiry.app.utils.http

class HttpCallback<S, E>(private val data: HttpState<S, E>) {
    fun onSuccess(fn: (HttpState.Success<S>) -> Unit) {
        if (data is HttpState.Success) {
            fn(data)
        }
    }

    fun onError(fn: (HttpState.Error<E>) -> Unit) {
        if (data is HttpState.Error) {
            fn(data)
        }
    }
}
