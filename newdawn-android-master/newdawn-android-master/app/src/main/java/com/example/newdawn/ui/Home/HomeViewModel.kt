package com.example.newdawn.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdawn.data.models.Job
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "HomeScreen"

data class HomeUiState(
    val userName: String = "",
    val recommendedJobs: List<Job> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application.applicationContext)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                // Name comes from DataStore so we don't burn an extra /api/auth/me call
                val name = tokenManager.getUserName() ?: "there"
                val authHeader = tokenManager.getAuthHeader()

                if (authHeader.isEmpty()) {
                    Log.d(TAG, "No token found - user is not logged in")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "You are not logged in. Please log in again."
                    )
                    return@launch
                }

                val response = RetrofitInstance.api.getJobs(authHeader)
                if (response.isSuccessful) {
                    val jobs = response.body().orEmpty()
                    // API returns newest first, so take(3) is "recommended"
                    val recommended = jobs.take(3)
                    Log.d(TAG, "Loaded ${jobs.size} jobs, showing ${recommended.size} recommended")
                    _uiState.value = _uiState.value.copy(
                        userName = name,
                        recommendedJobs = recommended,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    Log.e(TAG, "getJobs failed: HTTP ${response.code()}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Could not load jobs (HTTP ${response.code()})"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "getJobs network error", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message ?: "unknown"}"
                )
            }
        }
    }
}