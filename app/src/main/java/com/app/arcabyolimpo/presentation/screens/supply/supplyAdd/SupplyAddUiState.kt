package com.app.arcabyolimpo.presentation.screens.supply.supplyAdd

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.CategoryInfo
import com.app.arcabyolimpo.domain.model.supplies.WorkshopInfo
/** ---------------------------------------------------------------------------------------------- *
 * SuppliesAddUiState -> Data class with states that the view can have, such as the loding state
 * or an error had happen.
 * ---------------------------------------------------------------------------------------------- */
data class SupplyAddUiState(
    val workshops: List<WorkshopInfo> = emptyList(),
    val categories: List<CategoryInfo> = emptyList(),
    val name: String = "",
    val measureUnit: String = "",
    val selectedWorkshopId: String = "",
    val selectedCategoryId: String = "",
    val selectedImage: Uri? = null,
    val status: Int = 1,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
)
