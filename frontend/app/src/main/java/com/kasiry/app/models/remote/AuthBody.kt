package com.kasiry.app.models.remote

class AuthBody {
    data class Login (
        val email: String,
        val password: String
    )
}