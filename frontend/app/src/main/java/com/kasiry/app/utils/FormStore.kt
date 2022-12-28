package com.kasiry.app.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import arrow.core.Either
import arrow.core.left
import arrow.core.right

class Field <T>(
        val name: String,
        val initialValue: T,
        val rules: List<(Any) -> Either<String, Boolean>> = listOf()
    ) {
    private var _value by mutableStateOf(initialValue)
    var value: T
        get() = _value
        set(value) {
            _value = value
        }

    private var _errors by mutableStateOf(emptyList<String>())
    var errors: List<String>
        get() = _errors
        set(value) {
            _errors = value
        }

    private var _isFocused by mutableStateOf(false)
    var isFocused: Boolean
        get() = _isFocused
        set(value) {
            _isFocused = value
        }

    val focusRequest: FocusRequester = FocusRequester()
}

class FormStore(val fields: List<Field<Any>>) {
    var values = mutableStateListOf<Field<Any>>()
    var submitted by mutableStateOf(false)

    init {
        values.addAll(fields)
    }

    fun setValue(key: String, value: Any) {
        values.find { it.name == key }?.value = value
    }

    fun <T> getField(key: String): Field<T> {
        return values.find { it.name == key } as Field<T>
    }

    fun <T> getValue(key: String): T {
        return values.find { it.name == key }?.value as T
    }

    fun clearFocus() {
        values.forEach {
            it.isFocused = false
        }
    }

    fun handleSubmit(valid: () -> Unit) {
        submitted = true

        values.forEach { it ->
            it.errors = it.rules.map { rule ->
                when (val result = rule(it.value)) {
                    is Either.Left -> result.value
                    is Either.Right -> ""
                }
            }.filter { it.isNotEmpty() }
        }

        if (values.all { it.errors.isEmpty() }) {
            valid()
        }
    }

    fun validateField(key: String) {
        val field = values.find { it.name == key }
        field?.errors = field?.rules?.map { rule ->
            when (val result = rule(field.value)) {
                is Either.Left -> result.value
                is Either.Right -> ""
            }
        }?.filter { it.isNotEmpty() } ?: emptyList()
    }
}