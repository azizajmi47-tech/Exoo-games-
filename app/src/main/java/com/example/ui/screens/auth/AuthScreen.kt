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
                    "LOGIN", 
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = ExooTextPrimary,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("Use admin@exoo.com or user@exoo.com", fontSize = 12.sp, color = ExooAccentBlue)
                Spacer(modifier = Modifier.height(24.dp))
                
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
                            val success = viewModel.login(email)
                            if (success) {
                                onNavigateBack()
                            } else {
                                errorMessage = "Invalid email or password"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ExooAccentPurple),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("SIGN IN", fontWeight = FontWeight.Bold, color = ExooTextPrimary)
                }
            }
        }
    }
}
