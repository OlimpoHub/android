package com.app.arcabyolimpo.presentation.screens.supply.supplyUpdate

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.CategoryInfo
import com.app.arcabyolimpo.domain.model.supplies.WorkshopInfo

data class SupplyUpdateUiState(
    val workshops: List<WorkshopInfo> = emptyList(),
    val categories: List<CategoryInfo> = emptyList(),

    val name: String = "",
    val measureUnit: String = "",
    val selectedIdWorkshop: String? = null,
    val selectedIdCategory: String? = null,
    val selectedImageUrl: Uri? = null,
    val currentImageUrl: String? = null,
    val status: Int = 1,

    private val prevName: String = "",
    private val prevMeasureUnit: String = "",
    private val prevIdWorkshop: String? = null,
    private val prevIdCategory: String? = null,
    private val prevStatus: Int = 1,

    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,

    val nameError: String? = null,
    val measureUnitError: String? = null,
    val noCategory: String? = null,
    val noWorkshop: String? = null
) {
    val hadChanged: Boolean
        get() {
            if (selectedImageUrl != null) {
                return true
            }

            return name != prevName ||
                    measureUnit != prevMeasureUnit ||
                    selectedIdWorkshop != prevIdWorkshop ||
                    selectedIdCategory != prevIdCategory ||
                    status != prevStatus
        }

    fun loadSupplyData(
        name: String,
        measureUnit: String,
        status: Int,
        idWorkshop: String?,
        idCategory: String?,
        imageUrl: String?,
    ) = copy(
        name = name,
        measureUnit = measureUnit,
        status = status,
        selectedIdWorkshop = idWorkshop,
        selectedIdCategory = idCategory,
        currentImageUrl = imageUrl,

        prevName = name,
        prevMeasureUnit = measureUnit,
        prevStatus = status,
        prevIdWorkshop = idWorkshop,
        prevIdCategory = idCategory,
    )
}