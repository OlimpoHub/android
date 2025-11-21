package com.app.arcabyolimpo.presentation.screens.beneficiary

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto

data class ModifyBeneficiaryUiState (
    val modifiedBeneficiary: BeneficiaryDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)