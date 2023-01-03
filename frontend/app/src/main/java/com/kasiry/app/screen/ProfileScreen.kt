package com.kasiry.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kasiry.app.models.data.Profile
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray

@Composable
fun ProfileScreen(
    navController: NavController,
    profile: Profile
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .clip(
                        shape = RoundedCornerShape(999.dp)
                    )
                    .background(Color.blue())
                    .padding(16.dp)
                    .size(52.dp)
            )
            Text(
                text = profile.name,
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = profile.email,
                style = Typo.body,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp),
                color = Color.gray()
            )
        }
    }
}