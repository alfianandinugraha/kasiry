package com.kasiry.app.utils

import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusRequester
import arrow.core.Either

class Field <T>(
    var name: String = "",
    val initialValue: T,
    var rules: List<(Any) -> Either<String, Boolean>> = listOf()
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

class FormStore(val fields: Map<String, Field<Any>> = mutableStateMapOf()) {
    var submitted by mutableStateOf(false)

    init {
        fields.forEach { (key, value) ->
            fields[key]?.name = value.name
        }
    }

    fun setValue(key: String, value: Any) {
        fields[key]?.value = value
    }

    fun <T> getField(key: String, rules: List<(Any) -> Either<String, Boolean>> = listOf()): Field<T> {
        if (fields.containsKey(key)) {
            if (rules.isNotEmpty()) {
                fields[key]?.rules = rules
            }

            return fields[key] as Field<T>
        } else {
            throw Exception("Field $key not found")
        }
    }

    fun clearFocus() {
        fields.forEach {
            it.value.isFocused = false
        }
    }

    fun handleSubmit(valid: (fields: Map<String, Field<Any>>) -> Unit) {
        submitted = true

        fields.forEach { it ->
            it.value.errors = it.value.rules.map { rule ->
                when (val result = rule(it.value.value)) {
                    is Either.Left -> result.value
                    is Either.Right -> ""
                }
            }.filter { it.isNotEmpty() }
        }

        if (fields.all { it.value.errors.isEmpty() }) {
            valid(fields)
        }
    }

    fun validateField(key: String) {
        val field = fields[key]
        field?.errors = field?.rules?.map { rule ->
            when (val result = rule(field.value)) {
                is Either.Left -> result.value
                is Either.Right -> ""
            }
        }?.filter { it.isNotEmpty() } ?: emptyList()
    }
}