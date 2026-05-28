package com.example.ui.screens.cloud

import androidx.compose.foundation.background
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
import com.example.data.local.Game

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import com.example.ui.theme.*

@Composable
fun CloudGamingScreen(viewModel: ExooViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val genre by viewModel.selectedGenre.collectAsState()
    val games by viewModel.repository.getCloudGames(query, genre).collectAsState(initial = emptyList())
    
    val genres = listOf("Action", "RPG", "FPS", "Strategy", "Adventure")
    var isDropdownExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "CLOUD GAMING", 
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
            Text("No cloud games found.", color = ExooTextSecondary)
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

@Composable
fun GameCard(game: Game, viewModel: ExooViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    Card(
        colors = CardDefaults.cardColors(containerColor = ExooCard),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, ExooCardBorder)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFF1e293b))
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(game.name, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = ExooTextPrimary, maxLines = 1)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.background(
                            ExooAccentBlue.copy(alpha = 0.1f), 
                            RoundedCornerShape(4.dp)
                        ).border(1.dp, ExooAccentBlue.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(game.platform, color = ExooAccentBlue, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (game.rating > 0f) String.format("%.1f ★", game.rating) else "No Rating", fontSize = 10.sp, color = Color(0xFFFACC15))
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (currentUser != null && currentUser!!.role == "user") {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        for (i in 1..5) {
                            Icon(
                                imageVector = if (i <= (game.rating + 0.5f).toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = "Rate $i",
                                tint = Color(0xFFFACC15),
                                modifier = Modifier.size(16.dp).clickable {
                                    viewModel.rateGame(game, i)
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth().height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ExooAccentBlue),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text("PLAY NOW", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ExooBackground)
                }
            }
        }
    }
}
