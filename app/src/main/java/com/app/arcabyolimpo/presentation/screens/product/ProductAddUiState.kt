package com.app.arcabyolimpo.presentation.screens.product

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.CategoryInfo
import com.app.arcabyolimpo.domain.model.supplies.WorkshopInfo
/** ---------------------------------------------------------------------------------------------- *
 * SuppliesAddUiState -> Data class with states that the view can have, such as the loading state
 * or an error had happen.
 * ---------------------------------------------------------------------------------------------- */
data class ProductAddUiState(
    val workshops: List<WorkshopInfo> = emptyList(),
    val categories: List<CategoryInfo> = emptyList(),
    val name: String = "",
    val unitaryPrice: String = "",
    val description: String = "",
    val selectedWorkshopId: String = "",
    val selectedCategoryId: String = "",
    val selectedImage: Uri? = null,
    val status: Int = 1,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
)
