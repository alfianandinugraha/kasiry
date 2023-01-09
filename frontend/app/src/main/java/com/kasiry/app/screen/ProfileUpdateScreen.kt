package com.kasiry.app.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.TextField
import com.kasiry.app.compose.TopBar
import com.kasiry.app.models.data.Profile
import com.kasiry.app.rules.required
import com.kasiry.app.theme.Typo
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import com.kasiry.app.viewmodel.ProfileViewModel

@Composable
fun ProfileUpdateScreen(
    navController: NavController,
    profile: Profile,
    profileViewModel: ProfileViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val form = remember {
            FormStore(
                mapOf(
                    "name" to Field(
                        initialValue = profile.name,
                        rules = listOf(
                            required()
                        )
                    ),
                    "email" to Field(
                        initialValue = profile.email,
                        rules = listOf(
                            required()
                        )
                    ),
                    "phone" to Field(
                        initialValue = profile.phone,
                        rules = listOf(
                            required()
                        )
                    ),
                )
            )
        }
        TopBar(
            title = "Ubah Profil",
            onBack = {
                navController.popBackStack()
            },
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
        ) {
            TextField(
                label = "Nama Lengkap",
                control = form,
                name = "name",
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            TextField(
                label = "Email",
                control = form,
                name = "email",
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            TextField(
                label = "No Handphone",
                control = form,
                name = "phone",
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    form.handleSubmit {
                        val name = it["name"]?.value as String
                        val email = it["email"]?.value as String
                        val phone = it["phone"]?.value as String

                        val payload = Profile(
                            name = name,
                            email = email,
                            phone = phone,
                            abilities = profile.abilities,
                            company = profile.company,
                            companyId = profile.companyId,
                            userId = profile.userId,
                        )

                        profileViewModel.update(payload) {
                            onSuccess {
                                profileViewModel.setProfile(payload)
                                navController.popBackStack()
                            }
                        }
                    }
                }
            ) {
                Text(
                    text = "Simpan",
                    style = Typo.body,
                    color = Color.White
                )
            }
        }
    }
}