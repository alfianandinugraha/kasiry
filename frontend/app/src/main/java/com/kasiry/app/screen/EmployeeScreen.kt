package com.kasiry.app.screen

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.EmployeeItem
import com.kasiry.app.compose.TopBar
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
        viewModel.getAll()
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopBar(
            title = "Pegawai",
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
                navController.navigate("employees/create")
            },
        ) {
            Text(
                text = "Tambah Pegawai",
                color = Color.White,
                style = Typo.body
            )
        }
        Column(
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            when (val employeeState = employees) {
                is HttpState.Success -> {
                    employeeState.data.forEach { employee ->
                        EmployeeItem(
                            employee = employee,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                        )
                    }
                }
                is HttpState.Loading -> {
                    Text("Loading")
                }
                else -> {}
            }
        }
    }
}