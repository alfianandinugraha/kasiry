package com.kasiry.app.rules

fun required(): (Any) -> String {
    return fun (value: Any): String {
        value as String
        return if (value.isEmpty()) {
            "This field is required"
        } else {
            ""
        }
    }
}