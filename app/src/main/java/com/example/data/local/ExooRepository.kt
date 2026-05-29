package com.example.data.local

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExooRepository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext,
        ExooDatabase::class.java, "exoo-database"
    ).fallbackToDestructiveMigration(true).build()

    private val gameDao = db.gameDao()
    private val steamAccountDao = db.steamAccountDao()
    private val userDao = db.userDao()
    private val userRatingDao = db.userRatingDao()

    // --- Games ---
    fun getCloudGames(query: String = "", genre: String = ""): Flow<List<Game>> {
        return if (query.isEmpty() && genre.isEmpty()) gameDao.getGamesByType("cloud") 
        else gameDao.getGamesByTypeSearch("cloud", query, genre)
    }

    fun getFreeGames(query: String = "", genre: String = ""): Flow<List<Game>> {
        return if (query.isEmpty() && genre.isEmpty()) gameDao.getGamesByType("free") 
        else gameDao.getGamesByTypeSearch("free", query, genre)
    }

    fun getFeaturedGames(): Flow<List<Game>> = gameDao.getFeaturedGames()
    
    suspend fun addGame(game: Game) = gameDao.insertGame(game)
    suspend fun removeGame(id: Int) = gameDao.deleteGame(id)
    suspend fun rateGame(gameId: Int, userId: Int, rating: Int, game: Game) {
        userRatingDao.insertRating(UserRating(userId = userId, gameId = gameId, rating = rating))
        val avg = userRatingDao.getAverageRating(gameId) ?: 0f
        gameDao.insertGame(game.copy(rating = avg))
    }

    // --- Steam Accounts ---
    fun getSteamAccounts(query: String = ""): Flow<List<SteamAccount>> {
        return if (query.isEmpty()) steamAccountDao.getAllAccounts()
        else steamAccountDao.searchAccounts(query)
    }
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
