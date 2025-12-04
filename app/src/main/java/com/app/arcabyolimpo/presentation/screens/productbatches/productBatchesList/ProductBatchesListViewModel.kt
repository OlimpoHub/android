package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.usecase.productbatches.FilterProductBatchesUseCase
import com.app.arcabyolimpo.domain.usecase.productbatches.GetProductBatchesUseCase
import com.app.arcabyolimpo.domain.usecase.productbatches.SearchProductBatchesUseCase
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toUiModel
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/** ViewModel for ProductBatchesListScreen.
 * @param searchProductBatchesUseCase Use case to search for product batches based on a search term.
 * @param filterProductBatchesUseCase Use case to filter product batches based on a set of criteria.
 * @param getProductBatchesUseCase Use case to fetch the initial, unfiltered list of all product batches.
 */
@HiltViewModel
class ProductBatchesListViewModel
    @Inject
    constructor(
        private val searchProductBatchesUseCase: SearchProductBatchesUseCase,
        private val filterProductBatchesUseCase: FilterProductBatchesUseCase,
        private val getProductBatchesUseCase: GetProductBatchesUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ProductBatchesUiState(isLoading = true))
        val uiState: StateFlow<ProductBatchesUiState> = _uiState

        init {
            loadData()
        }

        /**
         * Loads the initial data for the screen.
         * This function fetches all product batches, extracts distinct values for filter options
         * (like names, prices, quantities), and updates the UI state with the results or
         * any errors that occur.
         */
        fun loadData() {
            viewModelScope.launch {
                _uiState.value = ProductBatchesUiState(isLoading = true)

                try {
                    val result = getProductBatchesUseCase()
                    val uiList = result.map { it.toUiModel() }
                    val nombres = uiList.map { it.nombre }.distinct()
                    val precios = uiList.map { it.precioVenta.toString() }.distinct()
                    val cantidades = uiList.map { it.cantidadProducida.toString() }.distinct()
                    val fechasRealizacion = uiList.mapNotNull { it.fechaRealizacion }.distinct()

                    val filterData =
                        FilterData(
                            sections =
                                mapOf(
                                    "nombre" to nombres,
                                    "precioVenta" to precios,
                                    "cantidadProducida" to cantidades,
                                    "fechaRealizacion" to fechasRealizacion,
                                ),
                        )

                    _uiState.value =
                        if (uiList.isEmpty()) {
                            ProductBatchesUiState(isLoading = false)
                        } else {
                            ProductBatchesUiState(
                                isLoading = false,
                                batches = uiList,
                                filterData = filterData,
                            )
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

        fun searchProductBatch(term: String) {
            viewModelScope.launch {
                if (term.isBlank()) {
                    loadData()
                } else {
                    searchProductBatchesUseCase(term).collect { result ->
                        when (result) {
                            is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                            is Result.Success ->
                                _uiState.update {
                                    it.copy(batches = result.data.map { it.toUiModel() }, isLoading = false, error = null)
                                }
                            is Result.Error ->
                                _uiState.update {
                                    it.copy(isLoading = false, error = result.exception.message)
                                }
                        }
                    }
                }
            }
        }

        fun filterProductBatch(filters: FilterDto) {
            _uiState.update { it.copy(filters = filters) }

            viewModelScope.launch {
                filterProductBatchesUseCase(filters).collect { result ->
                    when (result) {
                        is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                        is Result.Success ->
                            _uiState.update {
                                it.copy(batches = result.data.map { it.toUiModel() }, isLoading = false, error = null)
                            }
                        is Result.Error ->
                            _uiState.update {
                                it.copy(isLoading = false, error = result.exception.message)
                            }
                    }
                }
            }
        }

        fun clearFilters() {
            _uiState.update { it.copy(filters = FilterDto()) }
        }
    }
