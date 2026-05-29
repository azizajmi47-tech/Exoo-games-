package com.example.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import com.example.ExooViewModel
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AuthScreen(viewModel: ExooViewModel, onNavigateBack: () -> Unit) {
    var isLogin by remember { mutableStateOf(true) }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().background(ExooBackground), contentAlignment = Alignment.Center) {
        Card(
            modifier = Modifier.padding(32.dp).fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ExooCard),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, ExooCardBorder)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    if (isLogin) "LOGIN" else "REGISTER", 
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = ExooTextPrimary,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(if (isLogin) "Welcome back!" else "Create a new account (@gmail.com)", fontSize = 12.sp, color = ExooAccentBlue)
                Spacer(modifier = Modifier.height(24.dp))
                
                if (!isLogin) {
                    OutlinedTextField(
                        value = username,
                        onValueChange = { 
                            username = it
                            errorMessage = null 
                        },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ExooAccentPurple,
                            unfocusedBorderColor = ExooCardBorder,
                            focusedTextColor = ExooTextPrimary,
                            unfocusedTextColor = ExooTextPrimary,
                            focusedLabelColor = ExooAccentPurple,
                            unfocusedLabelColor = ExooTextSecondary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        errorMessage = null 
                    },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = errorMessage != null,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExooAccentPurple,
                        unfocusedBorderColor = ExooCardBorder,
                        focusedTextColor = ExooTextPrimary,
                        unfocusedTextColor = ExooTextPrimary,
                        focusedLabelColor = ExooAccentPurple,
                        unfocusedLabelColor = ExooTextSecondary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ExooAccentPurple,
                        unfocusedBorderColor = ExooCardBorder,
                        focusedTextColor = ExooTextPrimary,
                        unfocusedTextColor = ExooTextPrimary,
                        focusedLabelColor = ExooAccentPurple,
                        unfocusedLabelColor = ExooTextSecondary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(errorMessage!!, color = ExooError, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        coroutineScope.launch {
                            if (isLogin) {
                                val success = viewModel.login(email, password)
                                if (success) {
                                    onNavigateBack()
                                } else {
                                    errorMessage = "Invalid email or password"
                                }
                            } else {
                                if (email.isBlank() || password.isBlank() || username.isBlank()) {
                                    errorMessage = "Please fill all fields"
                                } else if (!email.endsWith("@gmail.com")) {
                                    errorMessage = "Please use a valid @gmail.com email"
                                } else {
                                    val success = viewModel.register(username, email, password)
                                    if (success) {
                                        onNavigateBack()
                                    } else {
                                        errorMessage = "Email already exists"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ExooAccentPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (isLogin) "SIGN IN" else "SIGN UP", fontWeight = FontWeight.Bold, color = ExooTextPrimary)
                }

                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = { isLogin = !isLogin; errorMessage = null }) {
                    Text(if (isLogin) "Don't have an account? Register" else "Already have an account? Login", color = ExooTextSecondary, fontSize = 12.sp)
                }
            }
        }
    }
}
