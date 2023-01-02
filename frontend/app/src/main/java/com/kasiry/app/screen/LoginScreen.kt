package com.kasiry.app.screen

import android.util.Log
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
import com.kasiry.app.models.remote.AuthBody
import com.kasiry.app.rules.minLength
import com.kasiry.app.rules.required
import com.kasiry.app.theme.*
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {
    val loginService = remember {
        LoginViewModel()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        val loginResponse by loginService.login.collectAsState()
        val isLoading = loginResponse is HttpState.Loading

        val form = remember {
            FormStore(
                fields = mutableStateMapOf(
                    "email" to Field(
                        initialValue = "",
                    ),
                    "password" to Field(
                        initialValue = "",
                    ),
                )
            )
        }

        val focusManager = LocalFocusManager.current

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
                    .padding(bottom = 2.dp)
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
                disabled = isLoading,
                modifier = remember {
                    Modifier.padding(bottom = 12.dp)
                },
                control = form,
                name = "email",
                startIcon = Icons.Rounded.Email,
                rules = remember {
                    listOf(
                        required()
                    )
                }
            )
            TextField(
                label = "Password",
                modifier = Modifier.padding(bottom = 16.dp),
                control = form,
                name = "password",
                startIcon = Icons.Rounded.Lock,
                rules = remember {
                    listOf(
                        required(),
                        minLength(8),
                    )
                }
            )
            Button(
                disabled = isLoading,
                onClick = remember(form, loginService, navController, focusManager) {
                    {
                        form.handleSubmit {
                            form.clearFocus()
                            focusManager.clearFocus()

                            val email = it["email"]?.value as String
                            val password = it["password"]?.value as String

                            loginService.login(
                                body = AuthBody.Login(
                                    email = email,
                                    password = password
                                )
                            ) {
                                onSuccess {
                                    navController.navigate("dashboard")
                                }

                                onError {
                                    Log.d("LoginScreen", "Login error: ${it.message}")
                                }
                            }
                        }
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
                text = remember {
                    buildAnnotatedString {
                        append("Belum punya akun? ")
                        withStyle(style = SpanStyle(color = Color.blue(500), fontWeight = FontWeight.Bold)) {
                            append("Daftar")
                        }
                    }
                },
                style = Typo.body,
                textAlign = TextAlign.Center,
                modifier = remember(navController) {
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                        .clickable {
                            navController.navigate("register")
                        }
                }
            )
            Text(
                text = remember {
                    buildAnnotatedString {
                        append("Lupa password? ")
                        withStyle(style = SpanStyle(color = Color.blue(500), fontWeight = FontWeight.Bold)) {
                            append("Reset")
                        }
                    }
                },
                style = Typo.body,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
            )
        }
    }
}