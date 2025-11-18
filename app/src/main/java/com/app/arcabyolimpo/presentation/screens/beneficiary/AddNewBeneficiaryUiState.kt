package com.app.arcabyolimpo.presentation.screens.beneficiary

import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary

data class AddNewBeneficiaryUiState(
    val addNewBeneficiary: Beneficiary? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false

    )

