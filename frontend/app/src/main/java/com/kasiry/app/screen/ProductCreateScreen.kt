package com.kasiry.app.screen

import android.app.Application
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.kasiry.app.compose.IconCircleButton
import com.kasiry.app.compose.Layout
import com.kasiry.app.compose.TopBar

@Composable
fun ProductCreateScreen(
    navController: NavController,
    application: Application
) {
    Layout(
        topbar = {
            TopBar(
                title = "Tambah Produk",
                onBack = {
                    navController.popBackStack()
                },
            )
        },
    ) {

    }
}