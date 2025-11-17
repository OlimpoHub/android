package com.app.arcabyolimpo.presentation.screens.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.usecase.product.GetProductsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductListUiState(
    val productsList: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val filterData: FilterData? = null,
    val selectedFilters: Map<String, List<String>> = emptyMap()
)

data class FilterData(
    val workshops: List<String> = emptyList(),
    val availability: List<String> = listOf("Disponible", "No disponible")
)

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsListUseCase: GetProductsListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        loadProductsList()
    }

    fun loadProductsList() {
        viewModelScope.launch {
            getProductsListUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        allProducts = result.data

                        // Extract unique workshops for filter
                        val workshops = result.data
                            .mapNotNull { it.workshopName }
                            .distinct()
                            .sorted()

                        _uiState.update {
                            it.copy(
                                productsList = result.data,
                                isLoading = false,
                                error = null,
                                filterData = FilterData(workshops = workshops)
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Error al cargar productos"
                            )
                        }
                    }
                }
            }
        }
    }

    fun filterProducts(filters: Map<String, List<String>>) {
        _uiState.update { it.copy(selectedFilters = filters) }

        val filtered = allProducts.filter { product ->
            val workshopMatch = filters["workshop"]?.isEmpty() != false ||
                    filters["workshop"]?.contains(product.workshopName) == true

            val availabilityMatch = filters["availability"]?.isEmpty() != false ||
                    (filters["availability"]?.contains("Disponible") == true && product.available) ||
                    (filters["availability"]?.contains("No disponible") == true && !product.available)

            workshopMatch && availabilityMatch
        }

        _uiState.update { it.copy(productsList = filtered) }
    }

    fun clearFilters() {
        _uiState.update {
            it.copy(
                selectedFilters = emptyMap(),
                productsList = allProducts
            )
        }
    }
}