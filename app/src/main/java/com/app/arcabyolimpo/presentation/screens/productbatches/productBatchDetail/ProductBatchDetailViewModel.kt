package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.usecase.productbatches.GetProductBatchUseCase
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductBatchDetailViewModel
    @Inject
    constructor(
        private val getProductBatchUseCase: GetProductBatchUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ProductBatchDetailUiState(isLoading = true))
        val uiState: StateFlow<ProductBatchDetailUiState> = _uiState

        fun loadBatch(id: String) {
            viewModelScope.launch {
                _uiState.value = uiState.value.copy(isLoading = true)
                try {
                    val batch = getProductBatchUseCase(id).toUiModel()
                    _uiState.value = ProductBatchDetailUiState(isLoading = false, batch = batch)
                } catch (e: Exception) {
                    _uiState.value =
                        ProductBatchDetailUiState(
                            isLoading = false,
                            error = "No se pudo cargar el lote",
                        )
                }
            }
        }
    }
