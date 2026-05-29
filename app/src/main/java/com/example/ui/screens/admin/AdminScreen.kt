package com.example.ui.screens.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.unit.sp
import com.example.ExooViewModel
import com.example.data.local.Game
import com.example.data.local.SteamAccount
import kotlinx.coroutines.launch
import com.example.ui.theme.*

@Composable
fun AdminScreen(viewModel: ExooViewModel) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Steam Accounts", "Emulators", "Ratings")

    Scaffold(
        containerColor = ExooBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = ExooCard,
                contentColor = ExooAccentPurple
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, color = if (selectedTab == index) ExooAccentPurple else ExooTextSecondary) }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                when (selectedTab) {
                    0 -> AddSteamAccountForm(viewModel, snackbarHostState)
                    1 -> AddEmulatorForm(viewModel, snackbarHostState)
                    2 -> ManageRatings(viewModel, snackbarHostState)
                }
            }
        }
    }
}

@Composable
fun AddSteamAccountForm(viewModel: ExooViewModel, snackbarHostState: SnackbarHostState) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var games by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var galleryImages by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUrl = it.toString() }
    }

    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetMultipleContents()) { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            galleryImages = uris.joinToString(",") { it.toString() }
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Add New Steam Account", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ExooTextPrimary)
        Spacer(modifier = Modifier.height(16.dp))
        
        AdminTextField(value = username, onValueChange = { username = it }, label = "Username")
        AdminTextField(value = password, onValueChange = { password = it }, label = "Password")
        AdminTextField(value = games, onValueChange = { games = it }, label = "Included Games (comma separated)")
        
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AdminTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = "Main Cover Image URL/URI", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { launcher.launch("image/*") }) {
                Icon(Icons.Default.Add, contentDescription = "Pick Image", tint = ExooAccentPurple)
            }
        }
        
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AdminTextField(value = galleryImages, onValueChange = { galleryImages = it }, label = "Gallery Images (comma separated)", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { galleryLauncher.launch("image/*") }) {
                Icon(Icons.Default.Add, contentDescription = "Pick Gallery Images", tint = ExooAccentPurple)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (username.isNotBlank()) {
                    coroutineScope.launch {
                        viewModel.repository.addSteamAccount(
                            SteamAccount(username = username, password = password, availableGames = games, avatarUrl = imageUrl, galleryImages = galleryImages)
                        )
                        snackbarHostState.showSnackbar("Steam Account Added")
                        username = ""; password = ""; games = ""; imageUrl = ""; galleryImages = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ExooAccentPurple)
        ) {
            Text("Add Account")
        }
    }
}

@Composable
fun AddEmulatorForm(viewModel: ExooViewModel, snackbarHostState: SnackbarHostState) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var downloadUrl by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUrl = it.toString() }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Add New Emulator", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ExooTextPrimary)
        Spacer(modifier = Modifier.height(16.dp))
        
        AdminTextField(value = name, onValueChange = { name = it }, label = "Emulator Name")
        AdminTextField(value = description, onValueChange = { description = it }, label = "Description")
        AdminTextField(value = downloadUrl, onValueChange = { downloadUrl = it }, label = "Download Link")
        
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            AdminTextField(value = imageUrl, onValueChange = { imageUrl = it }, label = "Image URL/URI", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { launcher.launch("image/*") }) {
                Icon(Icons.Default.Add, contentDescription = "Pick Image", tint = ExooAccentPurple)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (name.isNotBlank()) {
                    coroutineScope.launch {
                        viewModel.repository.addGame(
                            Game(name = name, description = description, actionUrl = downloadUrl, coverImageUrl = imageUrl, type = "cloud")
                        )
                        snackbarHostState.showSnackbar("Emulator Added")
                        name = ""; description = ""; downloadUrl = ""; imageUrl = ""
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ExooAccentPurple)
        ) {
            Text("Add Emulator")
        }
    }
}

@Composable
fun AdminTextField(value: String, onValueChange: (String) -> Unit, label: String, modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ExooAccentPurple,
            unfocusedBorderColor = ExooCardBorder,
            focusedTextColor = ExooTextPrimary,
            unfocusedTextColor = ExooTextPrimary,
            focusedLabelColor = ExooAccentPurple,
            unfocusedLabelColor = ExooTextSecondary
        ),
        singleLine = true
    )
}

@Composable
fun ManageRatings(viewModel: ExooViewModel, snackbarHostState: SnackbarHostState) {
    val allGames by remember { viewModel.repository.getFeaturedGames() }.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Game Ratings Management", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = ExooTextPrimary)
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(allGames.size) { index ->
                val game = allGames[index]
                OutlinedCard(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = ExooCard),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ExooCardBorder)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(game.name, fontWeight = FontWeight.Bold, color = ExooTextPrimary)
                            Text("Rating: ${if (game.rating > 0f) String.format("%.1f", game.rating) else "None"}", color = ExooTextSecondary)
                        }
                        TextButton(onClick = { 
                            coroutineScope.launch { 
                                viewModel.repository.addGame(game.copy(rating = 0f)) 
                                snackbarHostState.showSnackbar("Reset rating for ${game.name}")
                            } 
                        }) {
                            Text("Reset", color = ExooError)
                        }
                    }
                }
            }
        }
    }
}
