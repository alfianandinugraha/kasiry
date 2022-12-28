package com.kasiry.app.rules

fun maxLength(length: Number = 10): (Any) -> String {
    return fun (value: Any): String {
        value as String
        return if (value.length > length.toInt()) {
            "This field must be less than $length characters"
        } else {
            ""
        }
    }
}