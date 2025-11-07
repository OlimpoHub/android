package com.app.arcabyolimpo.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.local.auth.UserPreferences
import com.app.arcabyolimpo.domain.model.auth.UserRole
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel
    @Inject
    constructor(
        private val userPreferences: UserPreferences,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SplashUiState())
        val uiState = _uiState.asStateFlow()

        init {
            checkLocalSession()
        }

        private fun checkLocalSession() {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                try {
                    val id = userPreferences.getUserId().first()
                    val username = userPreferences.getUsername().first()
                    val roleStr = userPreferences.getUserRole().first()

                    if (id.isNullOrBlank() || username.isNullOrBlank() || roleStr.isNullOrBlank()) {
                        _uiState.value = SplashUiState(isLoading = false, role = null)
                        return@launch
                    }

                    val role = mapRoleStringToUserRole(roleStr)
                    _uiState.value = SplashUiState(isLoading = false, role = role)
                } catch (e: Exception) {
                    _uiState.value = SplashUiState(isLoading = false, role = null, error = e.message)
                }
            }
        }

        private fun mapRoleStringToUserRole(role: String): UserRole? =
            when (role.trim().uppercase()) {
                "COORD" -> UserRole.COORD
                "COLAB" -> UserRole.COLAB
                else -> null
            }
    }
