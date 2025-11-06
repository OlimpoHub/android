package com.app.arcabyolimpo.data.local.auth

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(UserPreferencesConstants.DATA_STORE_NAME)

/**
 * UserPreferences is responsible for managing user-related data such as authentication
 * tokens and basic user information (ID, username, role) using Jetpack DataStore.
 *
 * This class provides suspend functions for saving/updating data and Flow-based
 * functions for observing values reactively.
 */
class UserPreferences(
    private val context: Context,
) {
    companion object {
        private val KEY_ACCESS_TOKEN = stringPreferencesKey(UserPreferencesConstants.KEY_ACCESS_TOKEN)
        private val KEY_REFRESH_TOKEN = stringPreferencesKey(UserPreferencesConstants.KEY_REFRESH_TOKEN)

        private val KEY_USER_ID = stringPreferencesKey(UserPreferencesConstants.KEY_USER_ID)
        private val KEY_USERNAME = stringPreferencesKey(UserPreferencesConstants.KEY_USERNAME)
        private val KEY_USER_ROLE = stringPreferencesKey(UserPreferencesConstants.KEY_USER_ROLE)
    }

    /** Returns a Flow that emits the stored access token (or null if none exists). */
    fun getAccessToken(): Flow<String?> = context.dataStore.data.map { it[KEY_ACCESS_TOKEN] }

    /** Returns a Flow that emits the stored refresh token (or null if none exists). */
    fun getRefreshToken(): Flow<String?> = context.dataStore.data.map { it[KEY_REFRESH_TOKEN] }

    /** Saves both access and refresh tokens. Called after a successful login. */
    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String,
    ) {
        context.dataStore.edit {
            it[KEY_ACCESS_TOKEN] = accessToken
            it[KEY_REFRESH_TOKEN] = refreshToken
        }
    }

    /** Updates only the access token. Called after a successful refresh. */
    suspend fun updateAccessToken(newToken: String) {
        context.dataStore.edit {
            it[KEY_ACCESS_TOKEN] = newToken
        }
    }

    /** Returns a Flow that emits the stored user ID. */
    fun getUserId(): Flow<String?> = context.dataStore.data.map { it[KEY_USER_ID] }

    /** Returns a Flow that emits the stored username. */
    fun getUsername(): Flow<String?> = context.dataStore.data.map { it[KEY_USERNAME] }

    /** Returns a Flow that emits the stored user role. */
    fun getUserRole(): Flow<String?> = context.dataStore.data.map { it[KEY_USER_ROLE] }

    /** Saves the user information. Called after login to persist user session details. */
    suspend fun saveUser(
        id: String,
        username: String,
        role: String,
    ) {
        context.dataStore.edit {
            it[KEY_USER_ID] = id
            it[KEY_USERNAME] = username
            it[KEY_USER_ROLE] = role
        }
    }

    /** Clears all stored authentication and user data.
     * Called when logging out or when a session expires.
     */
    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
