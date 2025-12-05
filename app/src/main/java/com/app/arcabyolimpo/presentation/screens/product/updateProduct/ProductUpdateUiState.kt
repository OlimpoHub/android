package com.app.arcabyolimpo.presentation.screens.product.updateProduct

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.CategoryInfo
import com.app.arcabyolimpo.domain.model.supplies.WorkshopInfo

/**
 * ProductAddUiState -> Data class that holds the current state of the UI for the
 * product update screen.
 *
 * It contains all the data fields that the user can add as well as UI-specific states.
 */
data class ProductUpdateUiState(
    val workshops: List<WorkshopInfo> = emptyList(),
    val categories: List<CategoryInfo> = emptyList(),

    val name: String = "",
    val unitaryPrice: String = "",
    val description: String = "",
    val selectedIdWorkshop: String? = null,
    val selectedIdCategory: String? = null,
    val selectedImageUrl: Uri? = null,
    val currentImageUrl: String? = null,
    val status: Int = 1,

    private val prevName: String = "",
    private val prevUnitaryPrice: String = "",
    private val prevDescription: String = "",
    private val prevIdWorkshop: String? = null,
    private val prevIdCategory: String? = null,
    private val prevStatus: Int = 1,

    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
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
    ) {
    val hadChanged: Boolean
        get() {
            if (selectedImageUrl != null) {
                return true
            }

            return name != prevName ||
                    unitaryPrice != prevUnitaryPrice ||
                    description != prevDescription ||
                    selectedIdWorkshop != prevIdWorkshop ||
                    selectedIdCategory != prevIdCategory ||
                    status != prevStatus
        }

    fun loadProductData(
        name: String,
        unitaryPrice: String,
        description: String,
        status: Int,
        idWorkshop: String?,
        idCategory: String?,
        imageUrl: String?,
    ) = copy(
        name = name,
        unitaryPrice = unitaryPrice,
        description = description,
        status = status,
        selectedIdWorkshop = idWorkshop,
        selectedIdCategory = idCategory,
        currentImageUrl = imageUrl,

        prevName = name,
        prevUnitaryPrice = unitaryPrice,
        prevDescription = description,
        prevStatus = status,
        prevIdWorkshop = idWorkshop,
        prevIdCategory = idCategory,
    )
}