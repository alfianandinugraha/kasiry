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
fun ProductListScreen(
    navController: NavController,
    application: Application
) {
    Layout(
        topbar = {
            TopBar(
                title = "Produk",
                onBack = {
                    navController.popBackStack()
                },
            )
        },
        floatingButton = {
            IconCircleButton(
                icon = Icons.Rounded.Add,
                onClick = {
                    navController.navigate("products/create")
                }
            )
        }
    ) {

    }
}