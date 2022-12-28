package com.kasiry.app.rules

import arrow.core.Either

fun required(): (Any) -> Either<String, Boolean> {
    return fun (value: Any): Either<String, Boolean> {
        val isValid: Boolean

        when (value) {
            is String -> {
                isValid = value.isNotEmpty()
            }
            is Int -> {
                isValid = value > 0
            }
            is Double -> {
                isValid = value > 0
            }
            is Float -> {
                isValid = value > 0
            }
            is Long -> {
                isValid = value > 0
            }
            is Boolean -> {
                isValid = value
            }
            is List<*> -> {
                isValid = value.isNotEmpty()
            }
            is Map<*, *> -> {
                isValid = value.isNotEmpty()
            }
            is Set<*> -> {
                isValid = value.isNotEmpty()
            }
            is Array<*> -> {
                isValid = value.isNotEmpty()
            }
            else -> {
                return Either.Left("Invalid value")
            }
        }

        return if (!isValid) {
            Either.Left("This field is required")
        } else {
            Either.Right(true)
        }
    }
}