package com.kasiry.app.rules

fun minLength(length: Number): (Any) -> String {
    return fun (value: Any): String {
        value as String
        return if (value.length < length.toInt()) {
            "This field must be at least $length characters"
        } else {
            ""
        }
    }
}