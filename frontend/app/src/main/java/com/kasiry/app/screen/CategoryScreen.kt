package com.kasiry.app.screen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.*
import com.kasiry.app.repositories.CategoryRepository
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.CategoryViewModel
import org.koin.androidx.compose.get

@Composable
fun CategoryScreen(
    navController: NavController,
    application: Application
) {
    val categoryRepository: CategoryRepository = get()
    val viewModel = remember {
        CategoryViewModel(
            application,
            categoryRepository
        )
    }
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(true) {
        viewModel.getAll {
            onSuccess {
                Log.d("CategoryScreen", "onSuccess: $it")
            }
            onError {
                onError {
                    Toast
                        .makeText(
                            application.applicationContext,
                            "Gagal memuat semua kategori",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    navController.popBackStack()
                }
            }
        }
    }

    Layout(
        topbar = {
            TopBar(
                title = "Kategori",
                onBack = {
                    navController.popBackStack()
                },
            )
        },
        floatingButton = {
            IconCircleButton(icon = Icons.Rounded.Add) {
                navController.navigate("categories/create")
            }
        }
    ) {
        when(val categoriesState = categories) {
            is HttpState.Success -> {
                items(
                    count = categoriesState.data.size,
                ) {
                    val category = categoriesState.data[it]
                    CategoryItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .padding(horizontal = 32.dp),
                        category = category,
                        onClick = {
                            navController.navigate("categories/${category.categoryId}")
                        }
                    )
                }
            }
            is HttpState.Loading -> {
                item {
                    Loading()
                }
            }
            else -> {}
        }
    }
}