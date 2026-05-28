package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ExooRepository
import com.example.data.local.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExooViewModel(application: Application) : AndroidViewModel(application) {
    val repository = ExooRepository(application)
    
    // Auth State
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser
    
    init {
        // Create default admin on startup for demonstration
        viewModelScope.launch {
            if (repository.getUserByEmail("admin@exoo.com") == null) {
                repository.addUser(User(username = "Exoo Admin", email = "admin@exoo.com", role = "admin"))
            }
            if (repository.getUserByEmail("user@exoo.com") == null) {
                repository.addUser(User(username = "Player One", email = "user@exoo.com", role = "user"))
            }
        }
    }

    fun login(email: String) {
        viewModelScope.launch {
            val user = repository.getUserByEmail(email)
            if (user != null) {
                _currentUser.value = user
            }
        }
    }
    
    fun logout() {
        _currentUser.value = null
    }
}
