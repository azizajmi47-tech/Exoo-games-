package com.example.ui.screens.free

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ExooViewModel
import com.example.ui.screens.cloud.GameCard

import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import com.example.ui.theme.*

@Composable
fun FreeGamesScreen(viewModel: ExooViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val genre by viewModel.selectedGenre.collectAsState()
    val games by remember(query, genre) { viewModel.repository.getFreeGames(query, genre) }.collectAsState(initial = emptyList())
    
    val genres = listOf("Action", "RPG", "FPS", "Strategy", "Adventure")
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "FREE GAMES", 
                fontSize = 14.sp, 
                fontWeight = FontWeight.Bold, 
                color = ExooTextSecondary,
                letterSpacing = 1.sp
            )
            Box {
                TextButton(onClick = { isDropdownExpanded = true }) {
                    Text(if (genre.isEmpty()) "Filter: All" else "Filter: $genre", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ExooAccentBlue)
                }
                DropdownMenu(expanded = isDropdownExpanded, onDismissRequest = { isDropdownExpanded = false }) {
                    DropdownMenuItem(text = { Text("All Genres") }, onClick = { viewModel.updateGenre(""); isDropdownExpanded = false })
                    genres.forEach { g ->
                        DropdownMenuItem(text = { Text(g) }, onClick = { viewModel.updateGenre(g); isDropdownExpanded = false })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (games.isEmpty()) {
            Text("No free games found.", color = ExooTextSecondary)
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(games) { game -> GameCard(game, viewModel) }
            }
        }
    }
}
