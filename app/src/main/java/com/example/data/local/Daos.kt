package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games WHERE type = :type ORDER BY id DESC")
    fun getGamesByType(type: String): Flow<List<Game>>

    @Query("SELECT * FROM games WHERE isFeatured = 1 ORDER BY id DESC LIMIT 5")
    fun getFeaturedGames(): Flow<List<Game>>
    
    @Query("SELECT COUNT(*) FROM games")
    fun getGamesCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: Game)
    
    @Query("DELETE FROM games WHERE id = :id")
    suspend fun deleteGame(id: Int)
}

@Dao
interface SteamAccountDao {
    @Query("SELECT * FROM steam_accounts ORDER BY id DESC")
    fun getAllAccounts(): Flow<List<SteamAccount>>
    
    @Query("SELECT COUNT(*) FROM steam_accounts")
    fun getAccountsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: SteamAccount)
    
    @Query("DELETE FROM steam_accounts WHERE id = :id")
    suspend fun deleteAccount(id: Int)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY id DESC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
    
    @Query("SELECT COUNT(*) FROM users")
    fun getUsersCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)
    
    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteUser(id: Int)
}
