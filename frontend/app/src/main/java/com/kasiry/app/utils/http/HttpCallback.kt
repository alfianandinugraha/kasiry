package com.kasiry.app.utils.http

class HttpCallback<S>(private val data: HttpState<S>) {
    fun onSuccess(fn: (HttpState.Success<S>) -> Unit) {
        if (data is HttpState.Success) {
            fn(data)
        }
    }

    fun onError(fn: (HttpState.Error) -> Unit) {
        if (data is HttpState.Error) {
            fn(data)
        }
    }
}
