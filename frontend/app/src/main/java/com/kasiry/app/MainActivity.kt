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
import com.kasiry.app.screen.*
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
                Text(text = "Loading...")
            } else {
                val startDestination = if (profileState is HttpState.Success) "dashboard" else "login"
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("dashboard") {
                        DashboardScreen(
                            navController = navController,
                            profile = (profileState as HttpState.Success).data
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            navController = navController,
                            application = application,
                            profile = (profileState as HttpState.Success).data
                        )
                    }
                    composable("profile-update") {
                        ProfileUpdateScreen(
                            navController = navController,
                            application = application,
                            profile = (profileState as HttpState.Success).data
                        )
                    }
                    composable("login") {
                        LoginScreen(
                            navController = navController,
                            application = application
                        )
                    }
                    composable("register") {
                        RegisterScreen(
                            navController = navController
                        )
                    }
                    composable("company") {
                        (profileState as HttpState.Success).data.company?.let { it ->
                            CompanyScreen(
                                navController = navController,
                                company = it
                            )
                        }
                    }
                    composable("employees") {
                        EmployeeScreen(
                            navController = navController,
                            application = application
                        )
                    }
                    composable("employees/create") {
                        EmployeeCreateScreen(
                            navController = navController,
                            application = application
                        )
                    }
                    composable("company-update") {
                        (profileState as HttpState.Success).data.company?.let { it ->
                            CompanyUpdateScreen(
                                navController = navController,
                                company = it,
                                application = application
                            )
                        }
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