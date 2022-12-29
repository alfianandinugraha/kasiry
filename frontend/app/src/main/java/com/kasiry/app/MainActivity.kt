package com.kasiry.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kasiry.app.screen.DashboardScreen
import com.kasiry.app.screen.LoginScreen
import com.kasiry.app.screen.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "login"
            ) {
                composable("register") {
                    RegisterScreen(
                        navController = navController
                    )
                }

                composable("login") {
                    LoginScreen(
                        navController = navController
                    )
                }

                composable("dashboard") {
                    DashboardScreen (
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = name)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Greeting("Android")
}