package com.app.arcabyolimpo.presentation.screens.supply.supplyAdd

import android.net.Uri
import com.app.arcabyolimpo.domain.model.supplies.CategoryInfo
import com.app.arcabyolimpo.domain.model.supplies.WorkshopInfo

data class SupplyAddUiState(
    val workshops: List<WorkshopInfo> = emptyList(),
    val categories: List<CategoryInfo> = emptyList(),
    val name: String = "",
    val measureUnit: String = "",
    val selectedWorkshop: String = "",
    val selectedCategory: String = "",
    val selectedImage: Uri? = null,
    val status: Int = 1,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
)
