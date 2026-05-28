package com.example.ui.screens.steam

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ExooViewModel
import com.example.data.local.SteamAccount
import com.example.ui.theme.*

@Composable
fun SteamAccountsScreen(viewModel: ExooViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val accounts by viewModel.repository.getSteamAccounts(query).collectAsState(initial = emptyList())
    val currentUser by viewModel.currentUser.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "SHARED STEAM ACCOUNTS", 
                    fontSize = 14.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = ExooTextSecondary,
                    letterSpacing = 1.sp
                )
                Text("View All", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ExooAccentPurple)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (accounts.isEmpty()) {
            item { Text("No Steam accounts available.", color = ExooTextSecondary) }
        }

        items(accounts) { account ->
            SteamAccountCard(account, isLoggedIn = currentUser != null)
        }
    }
}

@Composable
fun SteamAccountCard(account: SteamAccount, isLoggedIn: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth().let { if (!account.isAvailable) it.fillMaxWidth(0.9f) else it },
        colors = CardDefaults.cardColors(containerColor = ExooCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ExooCardBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF1e293b), RoundedCornerShape(8.dp))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AccountCircle, contentDescription = null, tint = if (account.isAvailable) ExooAccentPurple else ExooAccentBlue)
                }
                
                Box(
                    modifier = Modifier.background(
                        color = if (account.isAvailable) ExooSuccess.copy(alpha = 0.1f) else ExooError.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(16.dp)
                    ).border(
                        1.dp, 
                        if (account.isAvailable) ExooSuccess.copy(alpha = 0.2f) else ExooError.copy(alpha = 0.2f),
                        RoundedCornerShape(16.dp)
                    ).padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        if (account.isAvailable) "Ready" else "In Use",
                        color = if (account.isAvailable) ExooSuccess else ExooError,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(account.username, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ExooTextPrimary)
            Text(account.availableGames, fontSize = 10.sp, color = ExooTextSecondary)
            
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* View details logic */ },
                modifier = Modifier.fillMaxWidth(),
                enabled = isLoggedIn,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ExooAccentPurple,
                    disabledContainerColor = Color(0xFF334155)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(if (isLoggedIn) "VIEW CREDENTIALS" else "LOGIN TO VIEW", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
