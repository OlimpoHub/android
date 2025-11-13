package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.usecase.productbatches.GetProductBatchesUseCase
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toUiModel
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProductBatchesListViewModel
    @Inject
    constructor(
        private val getProductBatchesUseCase: GetProductBatchesUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ProductBatchesUiState(isLoading = true))
        val uiState: StateFlow<ProductBatchesUiState> = _uiState

        init {
            loadData()
        }

        fun loadData() {
            viewModelScope.launch {
                _uiState.value = ProductBatchesUiState(isLoading = true)

                try {
                    val result = getProductBatchesUseCase()
                    val uiList = result.map { it.toUiModel() }

                    _uiState.value =
                        if (uiList.isEmpty()) {
                            ProductBatchesUiState(isLoading = false)
                        } else {
                            ProductBatchesUiState(isLoading = false, batches = uiList)
                        }
                } catch (e: Exception) {
                    val errorMessage =
                        when (e) {
                            is IOException -> "Error de conexión. Verifica tu red."
                            is HttpException -> "Error del servidor (${e.code()})."
                            else -> "Error inesperado: ${e.message}"
                        }

                    _uiState.value =
                        ProductBatchesUiState(
                            isLoading = false,
                            error = errorMessage,
                        )
                }
            }
        }
    }
