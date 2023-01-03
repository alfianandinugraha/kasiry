package com.kasiry.app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kasiry.app.screen.DashboardScreen
import com.kasiry.app.screen.LoginScreen
import com.kasiry.app.screen.RegisterScreen
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.MainViewModel
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = get()

        mainViewModel.getProfile {
            onSuccess {
                Log.d("MainActivity", "Profile onSuccess: $it")
            }
            onError {
                Log.d("MainActivity", "Profile onError: $it")
            }
        }

        setContent {
            val navController = rememberNavController()
            val profileState by mainViewModel.profile.collectAsState()

            if (profileState is HttpState.Loading || profileState === null) {
                Log.d("MainActivity", "onCreate")
            } else {
                Text(text = "Loading...")
                NavHost(
                    navController = navController,
                    startDestination = if (profileState is HttpState.Success) "dashboard" else "login"
                ) {
                    composable("register") {
                        RegisterScreen(
                            navController = navController
                        )
                    }

                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            application = application
                        )
                    }

                    composable("dashboard") {
                        DashboardScreen (
                            navController = navController,
                            profile = (profileState as HttpState.Success).data
                        )
                    }
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