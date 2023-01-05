package com.kasiry.app.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.*
import com.kasiry.app.models.data.Category
import com.kasiry.app.repositories.CategoryRepository
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.CategoryViewModel
import org.koin.androidx.compose.get

@Composable
fun CategoryPickerScreen(
    categoryId: String? = null,
    isOpen: Boolean = false,
    onBack: () -> Unit,
    navController: NavController,
    application: Application,
    onCategorySelected: (Category) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val categoryRepository: CategoryRepository = get()
    val viewModel = remember {
        CategoryViewModel(
            categoryRepository = categoryRepository,
            application = application
        )
    }
    val categories by viewModel.categories.collectAsState()

    val offset by animateFloatAsState(
        targetValue = if (isOpen) 0f else screenWidth.toFloat()
    )

    LaunchedEffect(isOpen) {
        if (isOpen) {
            viewModel.getAll {
                onError {
                    Toast
                        .makeText(
                            application.applicationContext,
                            "Gagal memuat semua kategori",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                    onBack()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .offset(x = offset.dp)
            .background(Color.White)
            .fillMaxSize()
    ) {
        Layout(
            topbar = {
                TopBar(
                    title = "Pilih Kategori",
                    onBack = onBack,
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
                            category = category,
                            withMore = false,
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(bottom = 12.dp),
                            onClick = {
                                onCategorySelected(category)
                            },
                            selected = categoryId == category.categoryId
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

}