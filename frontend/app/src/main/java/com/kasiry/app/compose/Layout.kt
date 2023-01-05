package com.kasiry.app.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Layout(
    modifier: Modifier = Modifier,
    topbar: @Composable (() -> Unit)? = null,
    floatingButton: @Composable (() -> Unit)? = null,
    content: LazyListScope.() -> Unit
) {
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            if (topbar != null) {
                item {
                    topbar()
                }
            }
            content(this)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 32.dp, end = 24.dp)
        ) {
            if (floatingButton != null) {
                floatingButton()
            }
        }
    }
}