package com.kasiry.app.screen

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.*
import com.kasiry.app.models.data.Employee
import com.kasiry.app.repositories.EmployeeRepository
import com.kasiry.app.theme.Typo
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.EmployeeViewModel
import org.koin.androidx.compose.get

@Composable
fun EmployeeScreen(
    navController: NavController,
    application: Application
) {
    val employeeRepository: EmployeeRepository = get()
    val viewModel = remember {
        EmployeeViewModel(
            application = application,
            employeeRepository = employeeRepository
        )
    }
    val employees by viewModel.employees.collectAsState()

    LaunchedEffect(true) {
        viewModel.getAll {
            onError {
                Toast
                    .makeText(
                        application.applicationContext,
                        "Gagal memuat semua pegawai",
                        Toast.LENGTH_SHORT
                    )
                    .show()
                navController.popBackStack()
            }
        }
    }

    Layout(
        topbar = {
            TopBar(
                title = "Pegawai",
                onBack = {
                    navController.popBackStack()
                },
            )
        },
        floatingButton = {
            IconCircleButton(icon = Icons.Rounded.Add) {
                navController.navigate("employees/create")
            }
        }
    ) {
        when (val employeeState = employees) {
            is HttpState.Success -> {
                items(employeeState.data.size) { idx ->
                    val employee = employeeState.data[idx]
                    EmployeeItem(
                        employee = employee,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .padding(horizontal = 32.dp),
                        onClick = {
                            navController.navigate("employees/${employee.userId}/update")
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