package com.kasiry.app.utils.http

data class HttpResponse<T>(
    val data: T,
    val message: String? = "",
)
