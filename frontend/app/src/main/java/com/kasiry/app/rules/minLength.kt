package com.kasiry.app.rules

import arrow.core.Either

fun minLength(length: Int): (Any) -> Either<String, Boolean> {
    return fun (value: Any): Either<String, Boolean> {
        return when (value)  {
            is String -> {
                if (value.length < length) {
                    Either.Left("This field must be at most $length characters")
                } else {
                    Either.Right(true)
                }
            }
            else -> {
                Either.Left("Invalid value")
            }
        }
    }
}