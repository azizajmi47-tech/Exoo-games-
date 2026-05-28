package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Game::class, SteamAccount::class, User::class, UserRating::class],
    version = 2,
    exportSchema = false
)
abstract class ExooDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun steamAccountDao(): SteamAccountDao
    abstract fun userDao(): UserDao
    abstract fun userRatingDao(): UserRatingDao
}
