package com.kasiry.app.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.TextField
import com.kasiry.app.rules.minLength
import com.kasiry.app.rules.required
import com.kasiry.app.theme.*
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore


@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        val form = remember {
            FormStore(
                fields = listOf(
                    Field(
                        "email",
                        "",
                        rules = listOf(
                            required(),
                        )
                    ),
                    Field(
                        "password",
                        "",
                        rules = listOf(
                            required(),
                            minLength(6),
                        )
                    )
                )
            )
        }

        val focusManager = LocalFocusManager.current

        val annotatedRegister = buildAnnotatedString {
            append("Belum punya akun? ")
            withStyle(style = SpanStyle(color = Color.blue(500), fontWeight = FontWeight.Bold)) {
                append("Daftar")
            }
        }

        val annotatedForgotPassword = buildAnnotatedString {
            append("Lupa password? ")
            withStyle(style = SpanStyle(color = Color.blue(500), fontWeight = FontWeight.Bold)) {
                append("Reset")
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 128.dp)
        ) {
            Text(
                text = "Masuk",
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
            )
            Text(
                text = "Harap masuk untuk melanjutkan",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = Typo.body,
                fontSize = 14.sp,
                color = Color.gray(500),
            )
            TextField(
                label = "Email",
                modifier = Modifier.padding(bottom = 12.dp),
                control = form,
                name = "email",
                startIcon = Icons.Rounded.Email
            )
            TextField(
                label = "Password",
                modifier = Modifier.padding(bottom = 16.dp),
                control = form,
                name = "password",
                startIcon = Icons.Rounded.Lock
            )
            Button(
                onClick = {
                    navController.navigate("dashboard")
                    form.handleSubmit {
                        form.clearFocus()
                        focusManager.clearFocus()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Masuk",
                    color = Color.White,
                    style = Typo.body
                )
            }
            Text(
                text = annotatedRegister,
                style = Typo.body,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable {
                        navController.navigate("register")
                    }
            )
            Text(
                text = annotatedForgotPassword,
                style = Typo.body,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }
    }
}