package com.app.arcabyolimpo.presentation.screens.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.interceptor.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SessionViewModel
    @Inject
    constructor(
        private val sessionManager: SessionManager,
    ) : ViewModel() {
        val username = sessionManager.getUsername().stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
        val role = sessionManager.getUserRole().stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

        fun logout() {
            viewModelScope.launch {
                sessionManager.logout()
            }
        }
    }
