package com.example.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ExooViewModel
import com.example.data.local.Game
import com.example.ui.theme.ExooAccentPurple

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun HomeScreen(viewModel: ExooViewModel) {
    val featuredGames by viewModel.repository.getFeaturedGames().collectAsState(initial = emptyList())
    val stats by viewModel.repository.getStats().collectAsState(initial = null)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            HeroSection()
        }
        
        item {
            stats?.let { StatsSection(it.totalGames, it.totalAccounts, it.totalUsers) }
        }

        item {
            Text("Featured Games", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        if (featuredGames.isEmpty()) {
            item {
                Text("No featured games available right now.", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            items(featuredGames) { game ->
                FeaturedGameCard(game)
            }
        }
    }
}

@Composable
fun HeroSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF1a1a2e), Color(0x446c63ff), Color(0x2200d4ff))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(ExooAccentBlue, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "FEATURED",
                            color = ExooBackground,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "★ 4.9",
                        color = Color(0xFFFACC15),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Cyberpunk 2077",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = ExooTextPrimary
                )
                Text(
                    "Phantom Liberty • Action RPG",
                    fontSize = 12.sp,
                    color = ExooTextSecondary
                )
            }
        }
    }
}

@Composable
fun StatsSection(games: Int, accounts: Int, users: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StatItem("STEAM ACC", "$accounts", ExooAccentPurple)
        Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFF1e293b)))
        StatItem("CLOUD GAMES", "$games", ExooAccentBlue)
        Box(modifier = Modifier.width(1.dp).height(32.dp).background(Color(0xFF1e293b)))
        StatItem("ACTIVE USERS", "$users", ExooTextPrimary)
    }
}

@Composable
fun StatItem(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = valueColor)
        Text(
            label,
            fontSize = 9.sp,
            color = ExooTextSecondary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun FeaturedGameCard(game: Game) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = ExooCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, ExooCardBorder)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFF1e293b), RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(game.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(game.type.uppercase(), fontSize = 10.sp, color = ExooTextSecondary)
            }
        }
    }
}
