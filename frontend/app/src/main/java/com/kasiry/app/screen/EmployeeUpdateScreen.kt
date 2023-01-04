package com.kasiry.app.screen

import android.app.Application
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.navigation.NavController
import com.kasiry.app.rules.required
import com.kasiry.app.utils.Field
import com.kasiry.app.utils.FormStore
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.kasiry.app.compose.*
import com.kasiry.app.models.data.Ability
import com.kasiry.app.models.data.Employee
import com.kasiry.app.repositories.EmployeeRepository
import com.kasiry.app.rules.minLength
import com.kasiry.app.theme.Typo
import com.kasiry.app.theme.blue
import com.kasiry.app.theme.red
import com.kasiry.app.utils.http.HttpState
import com.kasiry.app.viewmodel.EmployeeCreateViewModel
import com.kasiry.app.viewmodel.EmployeeUpdateViewModel
import com.kasiry.app.viewmodel.MainViewModel
import org.koin.androidx.compose.get

@Composable
fun EmployeeUpdateScreen(
    navController: NavController,
    application: Application,
    userId: String
) {
    val mainViewModel: MainViewModel = get()
    val profile by mainViewModel.profile.collectAsState()

    val viewModel = EmployeeUpdateViewModel(
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

    LaunchedEffect(userId) {
        viewModel.get(userId) {
            onSuccess {
                form.setValue("name", it.data.name)
                form.setValue("email", it.data.email)
                form.setValue("phone", it.data.phone)

                val abilities = it.data.abilities
                if (abilities.employee == true) form.setValue("employee", abilities.employee)
                if (abilities.product == true) form.setValue("product", abilities.product)
                if (abilities.transaction == true) form.setValue("transaction", abilities.transaction)
            }
        }
    }
    Column {
        var isShow by remember {
            mutableStateOf(false)
        }
        if (isShow) {
            Alert(
                title = "Hapus Pegawai",
                icon = Icons.Rounded.Delete,
                onClose = {
                    isShow = false
                }
            ) {
                Column {
                    Text(
                        text = "Apakah anda yakin ingin menghapus data ini?",
                        style = Typo.body,
                        textAlign = TextAlign.Center,
                    )
                    Button(
                        bgColor = { Color.red() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        onClick = {
                            viewModel.delete(userId) {
                                onSuccess {
                                    isShow = false
                                    navController.popBackStack()
                                }
                                onError {
                                    isShow = false
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Hapus",
                            style = Typo.body,
                            textAlign = TextAlign.Center,
                            color = Color.White,
                        )
                    }
                    Text(
                        text = "Batal",
                        style = Typo.body,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isShow = false
                            }
                            .padding(vertical = 14.dp),
                    )
                }

            }
        }

        TopBar(
            title = "Ubah Pegawai",
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
            when(val profileState = profile) {
                is HttpState.Success -> {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        onClick = {
                            form.handleSubmit {
                                val name = it["name"]?.value as String
                                val email = it["email"]?.value as String
                                val phone = it["phone"]?.value as String
                                val abilities = Ability(
                                    employee = it["employee"]?.value as Boolean,
                                    product = it["product"]?.value as Boolean,
                                    transaction = it["transaction"]?.value as Boolean,
                                )

                                viewModel.update(
                                    EmployeeRepository.UpdateBody(
                                        name = name,
                                        email = email,
                                        phone = phone,
                                        abilities = abilities,
                                        userId = userId,
                                    )
                                ) {
                                    onSuccess {
                                        navController.popBackStack()
                                    }
                                    onError {
                                        Toast
                                            .makeText(
                                                application.applicationContext,
                                                "Gagal mengubah pegawai",
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                }
                            }
                        }
                    ) {
                        Text(
                            text = "Simpan",
                            style = Typo.body,
                            color = Color.White,
                        )
                    }
                    Text(
                        text = "Hapus",
                        style = Typo.body,
                        color = Color.red(),
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .align(Alignment.CenterHorizontally)
                            .clickable {
                                isShow = true
                            }
                    )
                }
                else -> {}
            }
        }
    }
}