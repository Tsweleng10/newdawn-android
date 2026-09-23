package com.example.newdawn.ui.jobs

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

private const val TAG = "JobsScreen"

data class PostJobUiState(
    val title: String = "",
    val category: String = JOB_CATEGORIES.first(),
    val description: String = "",
    val location: String = "",
    val budget: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
) {
    /** Drives the enabled/disabled state of the submit button - no empty-field crashes. */
    val canSubmit: Boolean
        get() = title.isNotBlank() &&
            description.isNotBlank() &&
            location.isNotBlank() &&
            (budget.toDoubleOrNull() ?: 0.0) > 0.0 &&
            !isLoading
}

class PostJobViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application.applicationContext)

    private val _uiState = MutableStateFlow(PostJobUiState())
    val uiState: StateFlow<PostJobUiState> = _uiState.asStateFlow()

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value, errorMessage = null)
    }

    fun onCategoryChange(value: String) {
        _uiState.value = _uiState.value.copy(category = value)
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(description = value, errorMessage = null)
    }

    fun onLocationChange(value: String) {
        _uiState.value = _uiState.value.copy(location = value, errorMessage = null)
    }

    fun onBudgetChange(value: String) {
        // Only digits and one decimal point - keeps the numeric field clean
        if (value.isEmpty() || value.matches(Regex("^\\d*\\.?\\d*$"))) {
            _uiState.value = _uiState.value.copy(budget = value, errorMessage = null)
        }
    }

    fun submitJob() {
        val state = _uiState.value
        if (!state.canSubmit) {
            Log.d(TAG, "submitJob ignored - form is incomplete")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)
            try {
                val authHeader = tokenManager.getAuthHeader()
                if (authHeader.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "You are not logged in. Please log in again."
                    )
                    return@launch
                }

                val budgetValue = state.budget.toDoubleOrNull() ?: 0.0
                // If Job.budget is an Int: use state.budget.toIntOrNull() ?: 0
                val newJob = Job(
                    title = state.title.trim(),
                    category = state.category,
                    description = state.description.trim(),
                    location = state.location.trim(),
                    budget = budgetValue
                )

                Log.d(TAG, "Posting job '${newJob.title}' in ${newJob.location} for R$budgetValue")

                val response = RetrofitInstance.api.createJob(authHeader, newJob)
                if (response.isSuccessful) {
                    Log.d(TAG, "Job created successfully: id=${response.body()?.id}")
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                } else {
                    Log.e(TAG, "createJob failed: HTTP ${response.code()}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Could not post job (HTTP ${response.code()})"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "createJob network error", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Network error: ${e.message ?: "unknown"}"
                )
            }
        }
    }
}