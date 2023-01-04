package com.kasiry.app.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue

@Composable
fun Loading(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(
            color = Color.blue(),
            modifier = Modifier
                .size(42.dp)
        )
        Text(
            text = "Memuat...",
            color = Color.Gray,
            style = Typo.body,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}