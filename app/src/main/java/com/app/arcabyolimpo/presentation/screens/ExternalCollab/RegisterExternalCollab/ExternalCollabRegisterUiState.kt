package com.app.arcabyolimpo.presentation.screens.ExternalCollab.RegisterExternalCollab

data class ExternalCollabRegisterUiState(
    val firstName: String = "",
    val lastName: String = "",
    val secondLastName: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val degree: String = "",
    val roleId: Int = 3, // Default to external collaborator role
    val isActive: Boolean = true,
    val photoUrl: String? = null,

    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val successMessage: String? = null,

    // Validation errors
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null
)