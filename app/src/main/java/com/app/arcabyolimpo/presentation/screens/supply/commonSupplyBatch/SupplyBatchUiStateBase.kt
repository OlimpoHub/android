package com.app.arcabyolimpo.presentation.screens.supply.commonSupplyBatch

import com.app.arcabyolimpo.domain.model.supplies.Acquisition
import com.app.arcabyolimpo.domain.model.supplies.Supply

/**
 * Interface representing the base state of a supply batch UI.
 *
 * This interface defines the common properties and methods that all supply batch UI states should
 * implement.
 */
interface SupplyBatchUiStateBase {
    val suppliesList: List<Supply>
    val acquisitionTypes: List<Acquisition>
    val selectedSupplyId: String?
    val quantityInput: String
    val expirationDateInput: String
    val boughtDateInput: String
    val acquisitionInput: String
    val registerError: String?
    val registerSuccess: Boolean
    val error: String?
}
