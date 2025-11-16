package com.app.arcabyolimpo.presentation.screens.supply.commonSupplyBatch

import com.app.arcabyolimpo.domain.model.supplies.Acquisition
import com.app.arcabyolimpo.domain.model.supplies.Supply

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
