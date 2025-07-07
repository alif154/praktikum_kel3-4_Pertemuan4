package com.app.praktikum_kel3.screen

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.praktikum_kel3.model.request.RegisterRequest
import com.app.praktikum_kel3.navigation.Screen
import com.app.praktikum_kel3.service.api.ApiClient
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var fullNameError by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPasswordError by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Pembuatan Akun Baru",
                    style = MaterialTheme.typography.headlineSmall
                )

                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        fullNameError = false
                    },
                    isError = fullNameError,
                    label = { Text("Nama Lengkap") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (fullNameError) {
                    Text("Harap isi nama lengkap", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                        usernameError = false
                    },
                    isError = usernameError,
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (usernameError) {
                    Text("Username tidak boleh kosong", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = false
                    },
                    isError = emailError,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (emailError) {
                    Text("Masukkan email yang valid", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = false
                    },
                    isError = passwordError,
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (passwordError) {
                    Text("Password tidak boleh kosong", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        confirmPasswordError = false
                    },
                    isError = confirmPasswordError,
                    label = { Text("Ulangi Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                if (confirmPasswordError) {
                    Text("Password tidak cocok", color = MaterialTheme.colorScheme.error)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()

                        fullNameError = fullName.isBlank()
                        usernameError = username.isBlank()
                        emailError = email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        passwordError = password.isBlank()
                        confirmPasswordError = confirmPassword != password

                        if (!fullNameError && !usernameError && !emailError && !passwordError && !confirmPasswordError) {
                            isLoading = true
                            coroutineScope.launch {
                                try {
                                    val response = ApiClient.instance.register(
                                        RegisterRequest(
                                            nm_lengkap = fullName,
                                            email = email,
                                            username = username,
                                            password = password
                                        )
                                    )
                                    isLoading = false
                                    val body = response.body()

                                    if (response.isSuccessful && body != null) {
                                        Toast.makeText(context, "Berhasil mendaftar!", Toast.LENGTH_SHORT).show()
                                        navController.navigate(Screen.Login.route) {
                                            popUpTo(Screen.Register.route) { inclusive = true }
                                        }
                                    } else {
                                        val errorMsg = body?.message ?: response.message()
                                        Toast.makeText(context, "Gagal: $errorMsg", Toast.LENGTH_LONG).show()
                                    }
                                } catch (e: Exception) {
                                    isLoading = false
                                    Toast.makeText(context, "Terjadi error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Buat Akun")
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        navController.navigate(Screen.Login.route)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sudah punya akun? Masuk")
                }
            }
        }
    }

    if (isLoading) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            title = { Text("Sedang diproses") },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("Mengirim data ke server...")
                }
            }
        )
    }
}
