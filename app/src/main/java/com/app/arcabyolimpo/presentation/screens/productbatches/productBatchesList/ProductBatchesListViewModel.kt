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

        // Used only for testing with mock data
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
                                disponible = 1,
                                idInventario = "90ff826c-fade-426d-ae43-212390b4dd84",
                                precioVentaFormatted = "$20.50 MXN",
                                cantidadProducida = 30,
                                fechaCaducidadFormatted = "01 05 2025",
                                fechaRealizacionFormatted = "04 11 2025",
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
                            error = e.message ?: "Error desconocido, intenta de nuevo",
                        )
                }
            }
        }
    }
