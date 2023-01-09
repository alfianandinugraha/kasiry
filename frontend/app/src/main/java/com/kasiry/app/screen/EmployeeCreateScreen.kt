package com.kasiry.app.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.kasiry.app.compose.TextField
import com.kasiry.app.compose.TopBar
import com.kasiry.app.rules.required
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kasiry.app.compose.Button
import com.kasiry.app.compose.Checkbox
import com.kasiry.app.models.data.Ability
import com.kasiry.app.repositories.EmployeeRepository
import com.kasiry.app.rules.minLength
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.viewmodel.EmployeeCreateViewModel
import org.koin.androidx.compose.get

@Composable
fun EmployeeCreateScreen(
    navController: NavController,
    application: Application
) {
    val viewModel = EmployeeCreateViewModel(
        application = application,
        employeeRepository = get()
    )

    val form = remember {
        FormStore(
            mapOf(
                "name" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
                "email" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
                "phone" to Field(
                    initialValue = "",
                    rules = listOf(
                        required()
                    )
                ),
                "password" to Field(
                    initialValue = "",
                    rules = listOf(
                        required(),
                        minLength(8)
                    )
                ),
                "employee" to Field(
                    initialValue = false,
                ),
                "product" to Field(
                    initialValue = false,
                ),
                "transaction" to Field(
                    initialValue = false,
                ),
            )
        )
    }

    Column {
        TopBar(
            title = "Tambah Pegawai",
            onBack = {
                navController.popBackStack()
            },
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
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
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Informasi Pegawai",
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp)
            )
            TextField(
                label = "Nama",
                control = form,
                name = "name",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 8.dp),
            )
            TextField(
                label = "Email",
                control = form,
                name = "email",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )
            TextField(
                label = "No Handphone",
                control = form,
                name = "phone",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )
            TextField(
                label = "Password",
                control = form,
                name = "password",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
            )
            Text(
                text = "Hak Akses",
                style = Typo.body,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row {
                Checkbox(
                    control = form,
                    name = "transaction",
                    label = "Transaksi",
                    modifier = Modifier
                        .weight(1f)
                )
                Checkbox(
                    control = form,
                    name = "employee",
                    label = "Pegawai",
                    modifier = Modifier
                        .weight(1f)
                )
                Checkbox(
                    control = form,
                    name = "product",
                    label = "Produk",
                    modifier = Modifier
                        .weight(1f)
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = {
                    form.handleSubmit {
                        val name = it["name"]?.value as String
                        val email = it["email"]?.value as String
                        val phone = it["phone"]?.value as String
                        val password = it["password"]?.value as String
                        val abilities = Ability(
                            employee = it["employee"]?.value as Boolean,
                            product = it["product"]?.value as Boolean,
                            transaction = it["transaction"]?.value as Boolean,
                        )

                        viewModel.create(
                            EmployeeRepository.CreateBody(
                                name = name,
                                email = email,
                                phone = phone,
                                password = password,
                                abilities = abilities,
                            )
                        ) {
                            onSuccess {
                                navController.popBackStack()
                            }
                            onError {
                                Toast
                                    .makeText(
                                        application.applicationContext,
                                        "Gagal menambahkan pegawai",
                                        Toast.LENGTH_SHORT
                                    )
                                    .show()
                            }
                        }
                    }
                }
            ) {
                Text(
                    text = "Tambah",
                    style = Typo.body,
                    color = Color.White,
                )
            }
        }
    }
}