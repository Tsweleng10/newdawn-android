package com.example.newdawn.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SplashState {
    object Loading : SplashState()
    object Authenticated : SplashState()
    object Unauthenticated : SplashState()
}

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application.applicationContext)

    private val _state = MutableStateFlow<SplashState>(SplashState.Loading)
    val state: StateFlow<SplashState> = _state

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            // Add a small delay so the splash screen doesn't flash for 1 millisecond
            delay(1500)

            val token = tokenManager.getToken()
            if (token.isNullOrEmpty()) {
                _state.value = SplashState.Unauthenticated
            } else {
                _state.value = SplashState.Authenticated
            }
        }
    }
}