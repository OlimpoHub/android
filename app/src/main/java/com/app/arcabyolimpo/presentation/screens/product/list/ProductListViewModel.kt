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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * UI state for the Product list screen.
 */
data class ProductListUiState(
    val productsList: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,

    // Filter modal data (sections and options)
    val filterData: FilterData =
        FilterData(
            sections = emptyMap(),
        ),

    // Currently selected options in the filter modal
    val selectedFilters: FilterDto =
        FilterDto(
            filters = emptyMap(),
            order = "ASC",
        ),
)

/**
 * ViewModel responsible for loading the products list and applying
 * local filters (by workshop and alphabetical order).
 */
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val getProductsListUseCase: GetProductsListUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    /** Keeps the full list of products as it comes from the backend. */
    private var allProducts: List<Product> = emptyList()

    init {
        loadProductsList()
    }

    /**
     * Loads the complete products list from the use case and builds
     * the filter options for the modal.
     */
    fun loadProductsList() {
        viewModelScope.launch {
            getProductsListUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is Result.Success -> {
                        allProducts = result.data

                        // Build unique workshop names for the "Taller" filter
                        val workshops =
                            result.data
                                .mapNotNull { it.workshopName }
                                .distinct()
                                .sorted()

                        _uiState.update {
                            it.copy(
                                productsList = result.data,
                                isLoading = false,
                                error = null,
                                filterData =
                                    FilterData(
                                        sections =
                                            mapOf(
                                                // Only "Taller" filter is needed for products
                                                "Taller" to workshops,
                                            ),
                                    ),
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Error al cargar productos",
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Applies the selected filters locally over [allProducts].
     *
     * Supported filters:
     * - "Taller": one or more workshop names.
     * - Order: "ASC" or "DESC" (alphabetical order by product name).
     */
    fun applyFilters(filterDto: FilterDto) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                var filtered: List<Product> = allProducts

                // 1) Filter by workshop name ("Taller" section)
                val selectedWorkshops = filterDto.filters["Taller"]
                if (!selectedWorkshops.isNullOrEmpty()) {
                    filtered =
                        filtered.filter { product ->
                            product.workshopName != null &&
                                    selectedWorkshops.contains(product.workshopName)
                        }
                }

                // 2) Order alphabetically by product name
                val sorted =
                    when (filterDto.order) {
                        "DESC" ->
                            filtered.sortedByDescending { product ->
                                product.name.lowercase()
                            }

                        "ASC" ->
                            filtered.sortedBy { product ->
                                product.name.lowercase()
                            }

                        else -> filtered
                    }

                _uiState.update {
                    it.copy(
                        productsList = sorted,
                        selectedFilters = filterDto,
                        isLoading = false,
                    )
                }
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error aplicando filtros",
                    )
                }
            }
        }
    }

    /**
     * Clears all filters and restores the list to the original
     * products loaded from the backend.
     */
    fun clearFilters() {
        _uiState.update { current ->
            current.copy(
                selectedFilters = FilterDto(filters = emptyMap(), order = "ASC"),
                productsList = allProducts,
            )
        }
    }
}
