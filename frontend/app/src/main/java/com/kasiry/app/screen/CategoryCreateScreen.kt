package com.kasiry.app.screen

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Label
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.TextField
import com.kasiry.app.compose.TopBar
import com.kasiry.app.models.data.Ability
import com.kasiry.app.models.data.Category
import com.kasiry.app.repositories.CategoryRepository
import com.kasiry.app.repositories.EmployeeRepository
import com.kasiry.app.rules.minLength
import com.kasiry.app.rules.required
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.CategoryCreateViewModel
import com.kasiry.app.viewmodel.MainViewModel
import org.koin.androidx.compose.get

@Composable
fun CategoryCreateScreen(
    navController: NavController,
    application: Application
) {
    val mainViewModel: MainViewModel = get()
    val profile by mainViewModel.profile.collectAsState()

    val categoryRepository: CategoryRepository = get()
    val viewModel = CategoryCreateViewModel(
        application = application,
        categoryRepository = categoryRepository
    )

    val form = remember {
        FormStore(
            mapOf(
                "name" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
            )
        )
    }
    Column {
        TopBar(
            title = "Tambah Kategori",
            onBack = {
                navController.popBackStack()
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Label,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(999.dp)
                    )
                    .background(Color.blue())
                    .padding(16.dp)
                    .size(52.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Informasi Kategori",
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextField(
                label = "Nama",
                control = form,
                name = "name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 8.dp),
            )
            when(val profileState = profile) {
                is HttpState.Success -> {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        onClick = {
                            form.handleSubmit {
                                val name = it["name"]?.value as String

                                viewModel.create(
                                    Category(
                                        name = name,
                                        companyId = "",
                                        categoryId = ""
                                    )
                                ) {
                                    onSuccess {
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Tambah",
                            style = Typo.body,
                            color = Color.White,
                        )
                    }

                }
                else -> {}
            }
        }
    }
}