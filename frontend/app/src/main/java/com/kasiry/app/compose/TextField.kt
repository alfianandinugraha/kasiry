package com.kasiry.app.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.Either
import com.kasiry.app.theme.*
import com.kasiry.app.utils.FormStore

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    label: String,
    startIcon: ImageVector? = null,
    control: FormStore,
    name: String,
    disabled: Boolean = false,
    rules: List<(Any) -> Either<String, Boolean>> = listOf(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
    ),
    onChange: (String) -> Unit = {},
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    val field = remember(control, name, rules) {
        control.getField<String>(name, rules)
    }
    val value = field.value

    val focusRequester = remember {
        field.focusRequest
    }
    val isSubmitted = control.submitted

    val isFocused = field.isFocused
    val isError = field.errors.isNotEmpty()

    val fontColor by animateColorAsState(
        targetValue = if (isError) Color.red(500)
            else if (!isFocused) Color.gray(500)
            else Color.black(500)
    )
    val offsetY by animateFloatAsState(
        targetValue = if (value.isEmpty() && !isFocused) 0f else -24f
    )
    val textSize by animateFloatAsState(
        targetValue = if (value.isEmpty() && !isFocused) 16f else 14f
    )
    val valueOffsetX = if (startIcon != null) 32f else 0f
    val labelOffsetX by animateFloatAsState(
        targetValue = if (startIcon != null && value.isEmpty() && !isFocused) 32f else 0f
    )

    LaunchedEffect(isSubmitted, rules, name) {
        if (isSubmitted) {
            control.validateField(name)
        }
    }

    Column(modifier = modifier) {
        BasicTextField(
            value = field.value,
            enabled = !disabled,
            onValueChange = remember(field.value, control) {
                {
                    if (!disabled) {
                        onChange(it)
                        field.value = it
                        if (isSubmitted) {
                            control.validateField(name)
                        }
                    }

                }
            },
            keyboardOptions = keyboardOptions,
            modifier = remember(focusRequester, isFocused, disabled) {
                Modifier
                    .fillMaxWidth()
                    .focusable(enabled = !disabled)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (!disabled) {
                            field.isFocused = it.isFocused
                        }
                    }
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = Typo.body.fontFamily,
                color = if (disabled) Color.gray(500) else Color.black(500)
            ),
            visualTransformation = visualTransformation,
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .drawBehind {
                            val borderSize = 1.dp
                            drawLine(
                                color = fontColor,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = borderSize.toPx()
                            )
                        }
                        .padding(top = 16.dp, bottom = 12.dp)
                        .padding(top = 12.dp)
                ) {
                    if (startIcon != null) {
                        Icon(
                            imageVector = startIcon,
                            contentDescription = "",
                            tint = fontColor,
                            modifier = Modifier.width(22.dp)
                        )
                    }
                    Text(
                        text = label,
                        fontSize = textSize.sp,
                        color = fontColor,
                        style = Typo.body,
                        modifier = Modifier
                            .offset(labelOffsetX.dp, offsetY.dp)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = valueOffsetX.dp)
                    ) {
                        innerTextField()
                    }
                }
            }
        )
        if(isError) {
            Column {
                field.errors.forEach {
                    ErrorMessage(message = it)
                }
            }
        }
    }

}