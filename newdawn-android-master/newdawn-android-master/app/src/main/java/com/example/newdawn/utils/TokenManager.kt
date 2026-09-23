package com.example.newdawn.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "newdawn_prefs")

class TokenManager(private val context: Context) {

    companion object {
        val TOKEN_KEY = stringPreferencesKey("jwt_token")
        val USER_ID_KEY = stringPreferencesKey("user_id")
        val USER_NAME_KEY = stringPreferencesKey("user_name")
    }

    suspend fun saveToken(token: String, userId: Int, userName: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId.toString()
            prefs[USER_NAME_KEY] = userName
        }
    }

    suspend fun getToken(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[TOKEN_KEY]
    }

    suspend fun getAuthHeader(): String {
        val token = getToken() ?: return ""
        return "Bearer $token"
    }

    suspend fun getUserName(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[USER_NAME_KEY]
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}