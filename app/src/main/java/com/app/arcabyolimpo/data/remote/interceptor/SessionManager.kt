package com.app.arcabyolimpo.data.remote.interceptor

import com.app.arcabyolimpo.data.local.auth.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [SessionManager] handles the global session state of the user,
 * including logout events and token clearing.
 *
 * It provides a reactive mechanism to notify the app when the session expires,
 * allowing UI layers to respond by redirecting the user to the login screen.
 */
@Singleton
class SessionManager
    @Inject
    constructor(
        private val userPreferences: UserPreferences,
    ) {
        private val _sessionExpired = MutableSharedFlow<Unit>(replay = 0)
        val sessionExpired = _sessionExpired.asSharedFlow()

        /**
         * Performs a global logout by clearing all stored authentication data
         * and emitting a session expiration event to UI observers.
         */
        suspend fun logout() {
            _sessionExpired.emit(Unit)
            userPreferences.clearAll()
        }

        /** Returns the first two words of the stored username. */
        fun getUsername(): Flow<String> =
            userPreferences.getUsername().map { username ->
                username
                    ?.split(" ")
                    ?.take(2)
                    ?.joinToString(" ")
                    ?: ""
            }

        /** Returns the stored user role. */
        fun getUserRole(): Flow<String> =
            userPreferences.getUserRole().map { role ->
                role?.uppercase() ?: ""
            }
    }
