package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.usecase.productbatches.DeleteProductBatchUseCase
import com.app.arcabyolimpo.domain.usecase.productbatches.GetProductBatchUseCase
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the ProductBatchDetailScreen.
 *
 * This ViewModel is responsible for fetching the details of a specific product batch,
 * handling its deletion, and managing the UI state, including loading and error conditions.
 *
 * @param getProductBatchUseCase Use case to fetch the details of a single product batch by its ID.
 * @param deleteProductBatchUseCase Use case to delete a product batch by its ID.
 */
@HiltViewModel
class ProductBatchDetailViewModel
    @Inject
    constructor(
        private val getProductBatchUseCase: GetProductBatchUseCase,
        private val deleteProductBatchUseCase: DeleteProductBatchUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ProductBatchDetailUiState(isLoading = true))
        val uiState: StateFlow<ProductBatchDetailUiState> = _uiState

        /**
         * Loads the details of a specific product batch from the repository.
         *
         * It triggers a loading state, fetches the data using the getProductBatchUseCase,
         * maps the result to a UI model, and then updates the UI state with either the
         * loaded batch or an error message if the operation fails.
         *
         * @param id The unique identifier of the product batch to load.
         */
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

        /**
         * Deletes a specific product batch from the repository.
         *
         * This function calls the `deleteProductBatchUseCase` to perform the deletion. If the operation
         * fails, it updates the UI state with an appropriate error message.
         *
         * @param id The unique identifier of the product batch to be deleted.
         */
        fun deleteBatch(id: String) {
            viewModelScope.launch {
                try {
                    deleteProductBatchUseCase(id)
                } catch (e: Exception) {
                    _uiState.value =
                        ProductBatchDetailUiState(
                            isLoading = false,
                            error = "No se pudo eliminar el lote",
                        )
                }
            }
        }
    }
