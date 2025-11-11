package com.app.arcabyolimpo.presentation.screens.beneficiary

import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary


/*
 * UI state model que representa el estado de la pantalla de Detalle de Beneficiario.
 */
data class BeneficiaryDetailUiState (
    val isScreenLoading : Boolean = true,
    val beneficiary : Beneficiary? = null,
    val screenError: String? = null,
    val isDeleting: Boolean = false,
    val deleteError: String? = null,
    val deleteSuccess: Boolean = false
)