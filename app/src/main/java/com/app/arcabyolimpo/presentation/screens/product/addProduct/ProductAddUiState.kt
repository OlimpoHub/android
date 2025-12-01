package com.app.arcabyolimpo.presentation.screens.product.addProduct

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.CategoryInfo
import com.app.arcabyolimpo.domain.model.supplies.WorkshopInfo

/**
 * ProductAddUiState -> Data class that holds the current state of the UI for the
 * product addition screen.
 *
 * It contains all the data fields that the user can add as well as UI-specific states.
 */
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

    val isNameError: Boolean = false,
    val isUnitaryPriceError: Boolean = false,
    val isDescriptionError: Boolean = false,
    val isWorkshopError: Boolean = false,
    val isCategoryError: Boolean = false,
    val isImageError: Boolean = false,
    val isStatusError: Boolean = false,

    val nameErrorMessage: String = "",
    val unitaryPriceErrorMessage: String = "",
    val descriptionErrorMessage: String = "",
    val workshopErrorMessage: String = "",
    val categoryErrorMessage: String = "",
    val imageErrorMessage: String = "",
    val statusErrorMessage: String = "",
)
