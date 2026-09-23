package com.example.newdawn.ui.jobs

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.newdawn.data.models.Job
import com.example.newdawn.data.remote.RetrofitInstance
import com.example.newdawn.utils.TokenManager
import kotlinx.coroutines.Job as CoroutineJob   // alias so it doesn't clash with data.models.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "JobsScreen"

/** Categories offered in the filter chips and the Post Job dropdown. */
val JOB_CATEGORIES = listOf(
    "Gardening", "Cleaning", "Plumbing", "Electrical",
    "Painting", "Moving", "Childcare", "Cooking", "Other"
)

data class FindWorkUiState(
    val searchQuery: String = "",
    val selectedCategory: String? = null,   // null == "All"
    val selectedLocation: String = "",
    val jobs: List<Job> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val isEmptyResult: Boolean
        get() = !isLoading && errorMessage == null && jobs.isEmpty()
}

class FindWorkViewModel(application: Application) : AndroidViewModel(application) {

    private val tokenManager = TokenManager(application.applicationContext)

    private val _uiState = MutableStateFlow(FindWorkUiState())
    val uiState: StateFlow<FindWorkUiState> = _uiState.asStateFlow()

    // Restarted on every keystroke, so we only hit the API once the user pauses typing
    private var debounceJob: CoroutineJob? = null

    init {
        loadJobs()
    }

    fun onSearchChange(newQuery: String) {
        _uiState.value = _uiState.value.copy(searchQuery = newQuery)
        debounceAndLoad()
    }

    fun onCategoryChange(category: String?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        loadJobs()   // chips are a deliberate tap - no need to debounce
    }

    fun onLocationChange(location: String) {
        _uiState.value = _uiState.value.copy(selectedLocation = location)
        debounceAndLoad()
    }

    /** Called from the IME "Search" action so the user can force an immediate refresh. */
    fun applyFiltersNow() {
        debounceJob?.cancel()
        loadJobs()
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedCategory = null,
            selectedLocation = ""
        )
        loadJobs()
    }

    private fun debounceAndLoad() {
        debounceJob?.cancel()
        debounceJob = viewModelScope.launch {
            delay(400)   // wait for the user to stop typing
            loadJobs()
        }
    }

    fun loadJobs() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            try {
                val authHeader = tokenManager.getAuthHeader()
                if (authHeader.isEmpty()) {
                    Log.d(TAG, "No token - cannot load jobs")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "You are not logged in. Please log in again."
                    )
                    return@launch
                }

                val current = _uiState.value
                // Retrofit omits null query params, so blank filters simply aren't sent
                val response = RetrofitInstance.api.getJobs(
                    token = authHeader,
                    search = current.searchQuery.ifBlank { null },
                    category = current.selectedCategory,
                    location = current.selectedLocation.ifBlank { null }
                )

                if (response.isSuccessful) {
                    val jobs = response.body().orEmpty()
                    Log.d(
                        TAG,
                        "search='${current.searchQuery}' category=${current.selectedCategory} " +
                            "location='${current.selectedLocation}' -> ${jobs.size} jobs"
                    )
                    _uiState.value = _uiState.value.copy(
                        jobs = jobs,
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