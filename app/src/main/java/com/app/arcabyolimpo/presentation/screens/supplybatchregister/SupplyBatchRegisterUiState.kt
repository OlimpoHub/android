package com.app.arcabyolimpo.presentation.screens.supplybatchregister

import com.app.arcabyolimpo.domain.model.supplies.Supply

/**
 * UI state for the Supply Batch Register screen.
 */
data class SupplyBatchRegisterUiState(
	val suppliesList: List<Supply> = emptyList(),
	// Align naming with SuppliesListUiState (isLoading / error)
	val isLoading: Boolean = false,
	val error: String? = null,

	val selectedSupplyId: String? = null,
	val quantityInput: String = "",
	val expirationDateInput: String = "",
	val boughtDateInput: String = "",

	val registerLoading: Boolean = false,
	val registerError: String? = null,
	val registerSuccess: Boolean = false,
)