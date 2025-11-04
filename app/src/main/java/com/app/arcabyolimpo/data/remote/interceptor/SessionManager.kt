package com.app.arcabyolimpo.data.remote.interceptor

import com.app.arcabyolimpo.data.local.auth.UserPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
            userPreferences.clearAll()
            _sessionExpired.emit(Unit)
        }
    }
