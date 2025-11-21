package com.app.arcabyolimpo.presentation.screens.user.register

data class UserRegisterUiState (
    val firstName: String = "",
    val lastName: String = "",
    val secondLastName: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val degree: String = "",
    val roleId: String = "3",
    val isActive: Int = 1,
    val photoUrl: String? = null,
    val hasReglamentoInterno: Boolean = false,
    val hasCopiaIne: Boolean = false,
    val hasAvisoConfidencialidad: Boolean = false,

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

