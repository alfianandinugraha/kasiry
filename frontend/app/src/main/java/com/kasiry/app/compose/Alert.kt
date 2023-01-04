package com.kasiry.app.compose

import android.view.Gravity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.red

class AlertVariant {

}

@Composable
fun Alert(
    title: String,
    icon: ImageVector,
    color: Color = Color.red(),
    bgColor: Color= Color.red(50),
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onClose, properties = DialogProperties(dismissOnClickOutside = true)) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .fillMaxWidth(1f)
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .clip(
                            shape = RoundedCornerShape(999.dp)
                        )
                        .background(bgColor)
                        .padding(16.dp)
                        .size(32.dp),
                    tint = color
                )
                Text(
                    text = title,
                    style = Typo.body,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = 18.sp,
                    color = color
                )
            }
            content()
        }
    }
}