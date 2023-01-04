package com.kasiry.app.screen

import com.kasiry.app.models.data.Category
import android.app.Application
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.CategoryItem
import com.kasiry.app.compose.EmployeeItem
import com.kasiry.app.compose.TopBar
import com.kasiry.app.models.data.Employee
import com.kasiry.app.repositories.CategoryRepository
import com.kasiry.app.repositories.EmployeeRepository
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.CategoryViewModel
import com.kasiry.app.viewmodel.EmployeeViewModel
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
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopBar(
            title = "Kategori",
            onBack = {
                navController.popBackStack()
            },
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 16.dp),
            onClick = {
                navController.navigate("categories/create")
            },
        ) {
            Text(
                text = "Tambah Kategori",
                color = Color.White,
                style = Typo.body
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            when(val categoriesState = categories) {
                is HttpState.Success -> {
                    categoriesState.data.forEach { category ->
                        CategoryItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clickable {
                                    navController.navigate("categories/${category.categoryId}")
                                },
                            category = category
                        )
                    }
                }
            }
        }
    }
}