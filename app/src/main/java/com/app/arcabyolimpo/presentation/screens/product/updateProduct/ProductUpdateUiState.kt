package com.app.arcabyolimpo.presentation.screens.product.updateProduct

import android.net.Uri
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.model.supplies.CategoryInfo
import com.app.arcabyolimpo.domain.model.supplies.WorkshopInfo

data class ProductUpdateUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isProductLoaded: Boolean = false,

    val productDetail: Product? = null,

    val workshops: List<WorkshopInfo> = emptyList(),
    val categories: List<CategoryInfo> = emptyList(),

    val nameInput: String = "",
    val priceInput: String = "",
    val descriptionInput: String = "",
    val statusInput: Boolean = true,

    val selectedWorkshopId: String? = null,
    val selectedCategoryId: String? = null,
    val selectedImageUri: Uri? = null,

    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
)