package com.kasiry.app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Store
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.TopBar
import com.kasiry.app.models.data.Ability
import com.kasiry.app.models.data.Company
import com.kasiry.app.models.data.Profile
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.gray
import com.kasiry.app.theme.red
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.MainViewModel
import org.koin.androidx.compose.get

@Composable
fun CompanyScreen(
    company: Company,
    navController: NavController,
) {
    val mainViewModel: MainViewModel = get()
    val profile by mainViewModel.profile.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TopBar(
            title = "Usaha",
            onBack = {
                navController.popBackStack()
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Store,
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
                text = company.name,
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                text = company.phone,
                style = Typo.body,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp),
                color = Color.gray()
            )
            when (val profileState = profile) {
                is HttpState.Success -> {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .padding(top = 16.dp),
                        onClick = {
                            navController.navigate("company-update")
                        }
                    ) {
                        Text(
                            text = "Ubah",
                            style = Typo.body,
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    }
                }
                else -> {}
            }
        }
    }
}