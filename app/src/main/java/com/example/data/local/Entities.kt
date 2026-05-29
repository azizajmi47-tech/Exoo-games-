package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val email: String,
    val password: String = "",
    val role: String = "user" // can be "admin" or "user"
)

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val description: String,
    val coverImageUrl: String,
    val type: String, // "cloud" or "free"
    val platform: String = "",
    val genre: String = "",
    val actionUrl: String = "",
    val isFeatured: Boolean = false,
    val downloads: Int = 0,
    val rating: Float = 0f
)

@Entity(tableName = "steam_accounts")
data class SteamAccount(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String = "",
    val avatarUrl: String,
    val availableGames: String, // Comma separated string for simplicity
    val galleryImages: String = "", // Comma separated URIs for gallery images
    val isAvailable: Boolean = true
)

@Entity(tableName = "user_ratings")
data class UserRating(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val gameId: Int,
    val rating: Int
)
