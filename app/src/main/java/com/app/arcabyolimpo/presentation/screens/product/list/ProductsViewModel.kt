package com.app.arcabyolimpo.presentation.screens.product.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.domain.usecase.product.GetProductsListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for the products list screen.
 *
 * @property isLoading Indicates whether data is being loaded.
 * @property error Error message to display, if any.
 * @property products List of products currently visible (after filters).
 * @property allProducts Full list of products fetched from the backend.
 * @property searchQuery Current text in the search field.
 * @property filterData Data used to build the filter modal.
 * @property selectedFilters Filters currently selected by the user.
 */
data class ProductsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val products: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val searchQuery: String = "",
    val filterData: FilterData =
        FilterData(
            sections = emptyMap(),
        ),
    val selectedFilters: FilterDto =
        FilterDto(
            filters = emptyMap(),
            order = "ASC",
        ),
)

/**
 * ViewModel responsible for loading the products list and applying
 * search and filters (availability, workshop and ordering).
 *
 * @constructor Receives the use case [GetProductsListUseCase] to fetch products.
 */
@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsListUseCase: GetProductsListUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductsUiState> =
        MutableStateFlow(ProductsUiState())

    val uiState: StateFlow<ProductsUiState> = _uiState

    init {
        loadProducts()
    }

    /**
     * Loads the full products list from the backend and builds
     * the filter information (availability and workshop sections).
     */
    private fun loadProducts() {
        viewModelScope.launch {
            getProductsListUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = true,
                                error = null,
                            )
                        }
                    }

                    is Result.Success -> {
                        val allProducts: List<Product> = result.data

                        val workshopNames: List<String> =
                            allProducts
                                .mapNotNull { product -> product.workshopName }
                                .distinct()
                                .sorted()

                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = null,
                                allProducts = allProducts,
                                products = allProducts,
                                filterData =
                                    FilterData(
                                        sections =
                                            mapOf(
                                                "Disponibilidad" to listOf(
                                                    "Disponible",
                                                    "No disponible",
                                                ),
                                                "Taller" to workshopNames,
                                            ),
                                    ),
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(
                                isLoading = false,
                                error = "Error al cargar productos",
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates the search query typed by the user.
     *
     * @param newQuery New value for the search field.
     */
    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { currentState ->
            currentState.copy(searchQuery = newQuery)
        }
    }

    /**
     * Applies the selected filters from the modal (availability, workshop and order).
     *
     * @param dto [FilterDto] with the selected filters.
     */
    fun applyFilters(dto: FilterDto) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }

            try {
                val currentState: ProductsUiState = _uiState.value

                var filteredProducts: List<Product> = currentState.allProducts

                val availability: List<String>? = dto.filters["Disponibilidad"]
                if (!availability.isNullOrEmpty()) {
                    filteredProducts =
                        filteredProducts.filter { product ->
                            val isAvailable: Boolean = product.available
                            when {
                                "Disponible" in availability && isAvailable -> true
                                "No disponible" in availability && !isAvailable -> true
                                else -> false
                            }
                        }
                }

                val selectedWorkshops: List<String>? = dto.filters["Taller"]
                if (!selectedWorkshops.isNullOrEmpty()) {
                    filteredProducts =
                        filteredProducts.filter { product ->
                            product.workshopName != null &&
                                    selectedWorkshops.contains(product.workshopName)
                        }
                }

                val sortedProducts: List<Product> =
                    when (dto.order) {
                        "DESC" ->
                            filteredProducts.sortedByDescending { product ->
                                product.name.lowercase()
                            }

                        "ASC" ->
                            filteredProducts.sortedBy { product ->
                                product.name.lowercase()
                            }

                        else -> filteredProducts
                    }

                _uiState.update { currentState ->
                    currentState.copy(
                        selectedFilters = dto,
                        products = sortedProducts,
                        isLoading = false,
                    )
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = "Error aplicando filtros",
                    )
                }
            }
        }
    }

    /**
     * Clears all applied filters and restores the full products list.
     */
    fun clearFilters() {
        _uiState.update { currentState ->
            currentState.copy(
                selectedFilters =
                    FilterDto(
                        filters = emptyMap(),
                        order = "ASC",
                    ),
                products = currentState.allProducts,
            )
        }
    }
}
