package com.example.data.local

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExooRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        ExooDatabase::class.java, "exoo-database"
    ).build()

    private val gameDao = db.gameDao()
    private val steamAccountDao = db.steamAccountDao()
    private val userDao = db.userDao()

    // --- Games ---
    fun getCloudGames(): Flow<List<Game>> = gameDao.getGamesByType("cloud")
    fun getFreeGames(): Flow<List<Game>> = gameDao.getGamesByType("free")
    fun getFeaturedGames(): Flow<List<Game>> = gameDao.getFeaturedGames()
    
    suspend fun addGame(game: Game) = gameDao.insertGame(game)
    suspend fun removeGame(id: Int) = gameDao.deleteGame(id)

    // --- Steam Accounts ---
    fun getAllSteamAccounts(): Flow<List<SteamAccount>> = steamAccountDao.getAllAccounts()
    suspend fun addSteamAccount(account: SteamAccount) = steamAccountDao.insertAccount(account)
    suspend fun removeSteamAccount(id: Int) = steamAccountDao.deleteAccount(id)

    // --- Users ---
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()
    suspend fun getUserByEmail(email: String) = userDao.getUserByEmail(email)
    suspend fun addUser(user: User) = userDao.insertUser(user)
    suspend fun removeUser(id: Int) = userDao.deleteUser(id)
    
    // --- Stats ---
    fun getStats(): Flow<Stats> = kotlinx.coroutines.flow.combine(
        gameDao.getGamesCount(),
        steamAccountDao.getAccountsCount(),
        userDao.getUsersCount()
    ) { games, accounts, users ->
        Stats(games, accounts, users)
    }
}

data class Stats(
    val totalGames: Int = 0,
    val totalAccounts: Int = 0,
    val totalUsers: Int = 0
)
