package com.example.ui.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ExooViewModel
import com.example.data.local.Game
import com.example.data.local.SteamAccount
import kotlinx.coroutines.launch

@Composable
fun AdminScreen(viewModel: ExooViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val allGames by viewModel.repository.getFeaturedGames().collectAsState(initial = emptyList())

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Add sample data for demonstration
                coroutineScope.launch {
                    viewModel.repository.addGame(
                        Game(
                            name = "Cyberpunk", description = "Future City",
                            coverImageUrl = "", type = "cloud", platform = "GeForce Now", isFeatured = true, genre = "RPG"
                        )
                    )
                    viewModel.repository.addGame(
                        Game(
                            name = "CS:GO", description = "FPS",
                            coverImageUrl = "", type = "free", platform = "PC", genre = "FPS"
                        )
                    )
                    viewModel.repository.addSteamAccount(
                        SteamAccount(username = "Player1Steam", avatarUrl = "", availableGames = "GTA V, RDR 2")
                    )
                    snackbarHostState.showSnackbar("Sample data added")
                }
            }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(Icons.Default.Add, contentDescription = "Add Sample Data")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Admin Dashboard", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(24.dp))
            
            Text("Use the FAB to add sample games and accounts to the database.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Management Links", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(onClick = { /* TODO */ }) { Text("Manage Steam Accounts") }
                    TextButton(onClick = { /* TODO */ }) { Text("Manage Cloud Games") }
                    TextButton(onClick = { /* TODO */ }) { Text("Manage Free Games") }
                    TextButton(onClick = { /* TODO */ }) { Text("Manage Users") }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Game Ratings Management", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(allGames.size) { index ->
                    val game = allGames[index]
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(game.name, fontWeight = FontWeight.Bold)
                            Text("Current Rating: ${if (game.rating > 0f) String.format("%.1f", game.rating) else "None"}")
                        }
                        TextButton(onClick = { 
                            coroutineScope.launch { 
                                viewModel.repository.addGame(game.copy(rating = 0f)) 
                                snackbarHostState.showSnackbar("Reset rating for ${game.name}")
                            } 
                        }) {
                            Text("Reset", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }
        }
    }
}
