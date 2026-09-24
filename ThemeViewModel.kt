package com.example.uinewdawn.ui.theme.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yourapp.data.local.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel : ViewModel() {

    // Internal state — only this ViewModel can change it
    private val _themeMode = MutableStateFlow(TokenManager.getThemeModeBlocking())

    // Public state — the UI observes this
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    /**
     * Called when the user picks a theme.
     * Saves it to DataStore AND updates the in-memory state.
     */
    fun setThemeMode(mode: String) {
        _themeMode.value = mode

        viewModelScope.launch {
            TokenManager.saveThemeMode(mode)
        }
    }
}