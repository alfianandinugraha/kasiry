package com.kasiry.app.screen

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.EmployeeItem
import com.kasiry.app.compose.TopBar
import com.kasiry.app.theme.Typo

@Composable
fun EmployeeScreen(
    navController: NavController,
    application: Application
) {
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
            EmployeeItem(
                modifier = Modifier
                    .padding(bottom = 12.dp),
            )
            EmployeeItem(
                modifier = Modifier
                    .padding(bottom = 12.dp),
            )
            EmployeeItem(
                modifier = Modifier
                    .padding(bottom = 12.dp),
            )
        }
    }
}