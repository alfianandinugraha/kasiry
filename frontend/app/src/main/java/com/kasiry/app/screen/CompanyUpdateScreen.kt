package com.kasiry.app.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.TextField
import com.kasiry.app.compose.TopBar
import com.kasiry.app.models.data.Company
import com.kasiry.app.models.data.Profile
import com.kasiry.app.repositories.CompanyRepository
import com.kasiry.app.rules.required
import com.kasiry.app.theme.Typo
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.CompanyUpdateViewModel
import com.kasiry.app.viewmodel.MainViewModel
import org.koin.androidx.compose.get

@Composable
fun CompanyUpdateScreen(
    navController: NavController,
    company: Company,
    application: Application
) {
    val mainViewModel: MainViewModel = get()
    val profile by mainViewModel.profile.collectAsState()
    val viewModel = remember {
        CompanyUpdateViewModel(
            companyRepository = CompanyRepository(
                context = application.applicationContext
            ),
            application = application
        )
    }
    val form = remember {
        FormStore(
            mapOf(
                "name" to Field(
                    initialValue = company.name,
                    rules = listOf(
                        required()
                    )
                ),
                "address" to Field(
                    initialValue = company.address,
                    rules = listOf(
                        required()
                    )
                ),
                "phone" to Field(
                    initialValue = company.phone,
                    rules = listOf(
                        required()
                    )
                ),
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TopBar(
            title = "Ubah Usaha",
            onBack = {
                navController.popBackStack()
            },
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
        ) {
            TextField(
                label = "Nama Usaha",
                control = form,
                name = "name",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                label = "No Handphone",
                control = form,
                name = "phone",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                label = "Alamat",
                control = form,
                name = "address",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            when(val profileState = profile) {
                is HttpState.Success -> {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {
                            form.handleSubmit {
                                val name = it["name"]?.value as String
                                val address = it["address"]?.value as String
                                val phone = it["phone"]?.value as String
                                val newCompany = Company(
                                    companyId = company.companyId,
                                    name = name,
                                    address = address,
                                    phone = phone
                                )

                                viewModel.update(newCompany) {
                                    onSuccess {
                                        mainViewModel.setProfile(
                                            profile = Profile(
                                                profileState.data.userId,
                                                profileState.data.name,
                                                profileState.data.email,
                                                profileState.data.phone,
                                                profileState.data.abilities,
                                                profileState.data.companyId,
                                                company = newCompany
                                            )
                                        )
                                        navController.popBackStack()
                                    }
                                    onError {
                                        Toast
                                            .makeText(
                                                application.applicationContext,
                                                "Gagal mengubah usaha",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Simpan",
                            style = Typo.body,
                            color = Color.White
                        )
                    }
                }
                else -> {}
            }

        }
    }
}