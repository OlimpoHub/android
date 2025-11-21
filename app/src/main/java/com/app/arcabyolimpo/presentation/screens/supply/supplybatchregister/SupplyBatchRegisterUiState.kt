package com.app.arcabyolimpo.presentation.screens.supply.supplybatchregister

import com.app.arcabyolimpo.domain.model.supplies.Acquisition
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.presentation.screens.supply.commonSupplyBatch.SupplyBatchUiStateBase

/**
 * UI state for the Supply Batch Register screen.
 */
data class SupplyBatchRegisterUiState(
    override val suppliesList: List<Supply> = emptyList(),
    override val acquisitionTypes: List<Acquisition> = emptyList(),
    override val selectedSupplyId: String? = null,
    override val quantityInput: String = "",
    override val expirationDateInput: String = "",
    override val boughtDateInput: String = "",
    override val acquisitionInput: String = "",
    override val registerError: String? = null,
    override val error: String? = null,
    override val registerSuccess: Boolean = false,
    // Per-field validation errors (null when valid)
    val supplyError: String? = null,
    val quantityError: String? = null,
    val expirationDateError: String? = null,
    val boughtDateError: String? = null,
    val acquisitionError: String? = null,
    val isLoading: Boolean = false,
    val registerLoading: Boolean = false,
) : SupplyBatchUiStateBase
