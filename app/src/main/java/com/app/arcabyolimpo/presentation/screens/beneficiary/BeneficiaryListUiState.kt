package com.app.arcabyolimpo.presentation.screens.beneficiary

import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary


/**
 * Represents the UI state for the beneficiary list screen.
 *
 * @param isLoading True if data is currently being loaded.
 * @param beneficiaries The list of beneficiaries to display (potentially filtered)
 * @param error A message to display in case of an error.
 * @param searchText The current search query.
 */
data class BeneficiaryListUiState (
    val isLoading: Boolean = false,
    val beneficiaries: List<Beneficiary> = emptyList(),
    val error: String? = null,
    val searchText: String = ""
)