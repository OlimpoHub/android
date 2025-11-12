package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductBatchesListViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProductBatchesUiState(isLoading = true))
    val uiState: StateFlow<ProductBatchesUiState> = _uiState

    init {
        loadMockData()
    }

    private fun loadMockData() {
        viewModelScope.launch {
            try {
                delay(1000)

                val mockList =
                    List(8) {
                        ProductBatchUiModel(
                            idProducto = "p1",
                            nombre = "Lija",
                            precioUnitario = "26.00",
                            descripcion = "Lija fina",
                            imagen = "https://upload.wikimedia.org/wikipedia/commons/5/56/Sandpaper_grades_p120.jpg",
                            disponible = true,
                            idInventario = "90ff826c-fade-426d-ae43-212390b4dd84",
                            precioVenta = "20.50",
                            cantidadProducida = 30,
                            fechaCaducidad = "2026-05-01T06:00:00.000Z",
                            fechaRealizacion = "2025-11-04T06:00:00.000Z",
                        )
                    }

                _uiState.value =
                    ProductBatchesUiState(
                        isLoading = false,
                        batches = mockList,
                    )
            } catch (e: Exception) {
                _uiState.value =
                    ProductBatchesUiState(
                        isLoading = false,
                        error = e.message ?: "Error Desconocido, intenta de nuevo",
                    )
            }
        }
    }
}
