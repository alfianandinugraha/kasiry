package com.kasiry.app

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kasiry.app.compose.Loading
import com.kasiry.app.screen.*
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.AssetViewModel
import com.kasiry.app.viewmodel.MainViewModel
import com.kasiry.app.viewmodel.ProductViewModel
import com.kasiry.app.viewmodel.ProfileViewModel
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {
    lateinit var mainViewModel: MainViewModel
    lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = get()
        profileViewModel = get()

        profileViewModel.getProfile {
            onError {
                Toast.makeText(this@MainActivity, "Gagal mendapatkan profil", Toast.LENGTH_SHORT).show()
            }
        }

        setContent {
            val navController = rememberNavController()
            val profileState by profileViewModel.profileState.collectAsState()
            Log.d("MainActivity", "profileState: $profileState")


            when (val profile = profileState) {
                is HttpState.Loading -> {
                    Loading(
                        modifier = Modifier
                            .padding(top = 42.dp)
                    )
                }
                null -> {
                    Loading(
                        modifier = Modifier
                            .padding(top = 42.dp)
                    )
                }
                is HttpState.Error -> {
                    NavHost(navController = navController, startDestination = "login") {
                        composable("login") {
                            LoginScreen(
                                navController = navController,
                                application = application,
                                profileViewModel = get()
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                navController = navController
                            )
                        }
                    }
                }
                is HttpState.Success -> {
                    NavHost(navController = navController, startDestination = "dashboard") {
                        composable("dashboard") {
                            DashboardScreen(
                                navController = navController,
                                profile = profile.data,
                                cartViewModel = get(),
                                productViewModel = get(),
                                transactionViewModel = get()
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                navController = navController,
                                application = application,
                                profile = profile.data,
                                profileViewModel = get()
                            )
                        }
                        composable("profile-update") {
                            ProfileUpdateScreen(
                                navController = navController,
                                application = application,
                                profile = profile.data
                            )
                        }
                        composable("products") {
                            ProductListScreen(
                                navController = navController,
                                application = application,
                                productViewModel = get(),
                                cartViewModel = get()
                            )
                        }
                        composable("products/create") {
                            ProductCreateScreen(
                                navController = navController,
                                application = application,
                                assetViewModel = AssetViewModel(
                                    application,
                                    assetRepository = get()
                                ),
                                productViewModel = get(),
                                profile = profile.data
                            )
                        }
                        composable("products/{productId}/update") {
                            val productId = it.arguments?.getString("productId")
                            ProductUpdateScreen(
                                navController = navController,
                                application = application,
                                productViewModel = get(),
                                productId = productId!!
                            )
                        }
                        composable("company") {
                            profile.data.company?.let { it ->
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
                        composable("employees/{userId}/update") {
                            val userId = it.arguments?.getString("userId")
                            if (userId != null) {
                                EmployeeUpdateScreen(
                                    navController = navController,
                                    application = application,
                                    userId = userId
                                )
                            }
                        }
                        composable("categories") {
                            CategoryScreen(
                                navController = navController,
                                application = application,
                            )
                        }
                        composable("categories/create") {
                            CategoryCreateScreen(
                                navController = navController,
                                application = application
                            )
                        }
                        composable("categories/{categoryId}") {
                            val categoryId = it.arguments?.getString("categoryId")
                            if (categoryId != null) {
                                CategoryUpdateScreen(
                                    navController = navController,
                                    application = application,
                                    categoryId = categoryId
                                )
                            }
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
                        composable("cart") {
                            CartListScreen(
                                navController = navController,
                                application = application,
                                cartViewModel = get(),
                                productViewModel = get(),
                                transactionViewModel = get()
                            )
                        }
                        composable("transactions/{transactionId}") {
                            val transactionId = it.arguments?.getString("transactionId")!!
                            TransactionDetailScreen(
                                navController = navController,
                                application = application,
                                transactionViewModel = get(),
                                transactionId = transactionId
                            )
                        }
                        composable("transactions") {
                            TransactionListScreen(
                                navController = navController,
                                application = application,
                                transactionViewModel = get(),
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