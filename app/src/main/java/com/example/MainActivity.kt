package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.ui.navigation.*
import com.example.ui.screens.admin.AdminScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.cloud.CloudGamingScreen
import com.example.ui.screens.free.FreeGamesScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.steam.SteamAccountsScreen
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.automirrored.filled.*
import com.example.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ExooApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExooApp() {
    val navController = rememberNavController()
    val viewModel: ExooViewModel = viewModel()
    val currentUser by viewModel.currentUser.collectAsState()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        containerColor = ExooBackground,
        topBar = {
            Column {
                TopAppBar(
                    title = { 
                        Column {
                            Text(
                                "EXOO GAMES", 
                                fontWeight = FontWeight.Bold, 
                                style = TextStyle(
                                    brush = Brush.linearGradient(
                                        colors = listOf(ExooAccentPurple, ExooAccentBlue)
                                    )
                                ),
                                letterSpacing = 2.sp
                            )
                            Text(
                                "PREMIUM PORTAL",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = ExooTextSecondary,
                                letterSpacing = 2.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ExooBackground,
                        titleContentColor = ExooTextPrimary
                    ),
                    actions = {
                        if (currentUser == null) {
                            TextButton(onClick = { navController.navigate(Routes.Auth) }) {
                                Text("LOGIN", color = ExooTextPrimary)
                            }
                        } else {
                            if (currentUser?.role == "admin") {
                                IconButton(onClick = { navController.navigate(Routes.Admin) }, modifier = Modifier.clip(CircleShape).background(ExooCard)) {
                                    Icon(Icons.Default.Settings, contentDescription = "Admin Area", tint = ExooTextSecondary)
                                }
                            }
                            IconButton(onClick = { viewModel.logout() }, modifier = Modifier.padding(start = 8.dp, end = 8.dp).clip(CircleShape).background(Brush.linearGradient(listOf(ExooAccentPurple, Color(0xFF4b44cc))))) {
                                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = ExooTextPrimary)
                            }
                        }
                    }
                )
                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).height(50.dp),
                    placeholder = { Text("Search games, accounts...", color = ExooTextSecondary, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = ExooTextSecondary, modifier = Modifier.size(20.dp)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ExooCard,
                        unfocusedContainerColor = ExooCard,
                        focusedBorderColor = ExooAccentPurple,
                        unfocusedBorderColor = ExooCardBorder,
                        focusedTextColor = ExooTextPrimary,
                        unfocusedTextColor = ExooTextPrimary
                    ),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(25.dp),
                    singleLine = true
                )
            }
        },
        bottomBar = {
            val items = listOf(
                Pair("Home", Routes.Home) to Icons.Default.Home,
                Pair("Steam", Routes.SteamAccounts) to Icons.Default.AccountCircle,
                Pair("Cloud", Routes.CloudGaming) to Icons.Default.Cloud,
                Pair("Free", Routes.FreeGames) to Icons.Default.SportsEsports
            )
            NavigationBar(
                containerColor = ExooBottomNav,
                contentColor = ExooTextSecondary,
                tonalElevation = 0.dp
            ) {
                items.forEach { (navInfo, icon) ->
                    val (label, route) = navInfo
                    val isSelected = currentDestination?.route == route
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                        selected = isSelected,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ExooAccentPurple,
                            selectedTextColor = ExooAccentPurple,
                            indicatorColor = ExooAccentPurple.copy(alpha = 0.1f),
                            unselectedIconColor = ExooTextSecondary,
                            unselectedTextColor = ExooTextSecondary
                        ),
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.Home,
            modifier = Modifier.padding(innerPadding).fillMaxSize()
        ) {
            composable(Routes.Home) { HomeScreen(viewModel) }
            composable(Routes.SteamAccounts) { SteamAccountsScreen(viewModel) }
            composable(Routes.CloudGaming) { CloudGamingScreen(viewModel) }
            composable(Routes.FreeGames) { FreeGamesScreen(viewModel) }
            composable(Routes.Auth) { AuthScreen(viewModel, onNavigateBack = { navController.popBackStack() }) }
            composable(Routes.Admin) { AdminScreen(viewModel) }
        }
    }
}
