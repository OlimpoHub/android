package com.app.arcabyolimpo.presentation.screens.product.updateProduct

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.model.product.ProductUpdate
import com.app.arcabyolimpo.domain.usecase.product.GetProductUseCase
import com.app.arcabyolimpo.domain.usecase.product.UpdateProductUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetWorkshopCategoryInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.net.toUri

@HiltViewModel
class UpdateProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val getWorkshopCategoryInfoUseCase: GetWorkshopCategoryInfoUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUpdateUiState())
    val state: StateFlow<ProductUpdateUiState> = _uiState.asStateFlow()

    private val productId: String = savedStateHandle.get<String>("productId") ?: ""

    init {
        viewModelScope.launch {
            loadDropDownData()
            loadProductDetails()
        }
    }

    private fun loadDropDownData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getWorkshopCategoryInfoUseCase()

            _uiState.update {
                if (result.isSuccess) {
                    val data = result.getOrNull()
                    it.copy(
                        isLoading = false,
                        workshops = data?.workshops ?: emptyList(),
                        categories = data?.categories ?: emptyList(),
                    )
                }else {
                    it.copy(
                        isLoading = false,
                        error = "Error cargando datos",
                    )
                }
            }
        }
    }

    private fun loadProductDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val infoResult = getWorkshopCategoryInfoUseCase.invoke()

            if (infoResult.isFailure) {
                // Manejo de error si falla la carga de info base
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar info base: ${infoResult.exceptionOrNull()?.message}"
                    )
                }
                return@launch // Sale de la coroutine
            }

            // Desestructurar y obtener las listas de Talleres y Categorías del resultado exitoso
            // ESTO SOLUCIONA EL PROBLEMA DE ALCANCE (SCOPE)
            val (categories, workshops) = infoResult.getOrThrow()

            // 2. OBTENER DETALLES DEL PRODUCTO
            getProductUseCase(productId).onSuccess { productDetail ->

                // **USO DE PROPIEDADES CONFIRMADAS:** .name y .type
                val selectedWorkshop = workshops.find { it.name == productDetail.workshopName }
                val selectedCategory = categories.find { it.type == productDetail.categoryDescription }

                _uiState.update { state ->
                    state.copy(
                        // Se actualizan las listas en el estado
                        workshops = workshops,
                        categories = categories,

                        productDetail = productDetail,
                        isProductLoaded = true,

                        // PRE-LLENAR TODOS LOS CAMPOS DE LA UI
                        name = productDetail.name,
                        unitaryPrice = productDetail.unitaryPrice,
                        description = productDetail.description,
                        status = productDetail.status, // Int

                        // ASIGNAR IDs
                        selectedWorkshopId = selectedWorkshop?.idWorkshop,
                        selectedCategoryId = selectedCategory?.idCategory,

                        // Convertir la URL (String) a Uri
                        selectedImageUri = productDetail.image?.toUri(),
                        isLoading = false
                    )
                }
            }.onFailure { e ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Error al cargar el producto: ${e.message}"
                    )
                }
            }
        }
    }

    /**
     * Construye el modelo ProductUpdate a partir de los inputs y llama al caso de uso de actualización.
     */
    // UpdateProductViewModel.kt

    fun onSaveClick() {
        val state = _uiState.value

        if (state.name.isBlank() || state.selectedWorkshopId == null || state.selectedCategoryId == null) {
            _uiState.update { it.copy(error = "Completa todos los campos obligatorios") }
            return
        }

        val productToUpdate = ProductUpdate(
            name = state.name,
            unitaryPrice = state.unitaryPrice,
            description = state.description,
            status = state.status.toString(),
            idWorkshop = state.selectedWorkshopId!!,
            idCategory = state.selectedCategoryId!!,
            image = state.selectedImageUri
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, error = null) }

            val result = updateProductUseCase(productId, productToUpdate)

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        isSaving = false,
                        saveSuccess = true,
                    )
                } else {
                    it.copy(
                        isSaving = false,
                        error = "Error al actualizar: ${result.exceptionOrNull()?.message}"
                    )
                }
            }
        }
    }

    fun onNameChange(name: String) = _uiState.update { it.copy(name = name) }
    fun onUnitaryPriceChange(price: String) = _uiState.update { it.copy(unitaryPrice = price) }
    fun onDescriptionChange(description: String) = _uiState.update { it.copy(description = description) }
    fun onStatusChange(status: Int) = _uiState.update { it.copy(status = status) }
    fun onWorkshopSelected(id: String) = _uiState.update { it.copy(selectedWorkshopId = id) }
    fun onCategorySelected(id: String) = _uiState.update { it.copy(selectedCategoryId = id) }
    fun onImageSelected(uri: Uri?) = _uiState.update { it.copy(selectedImageUri = uri) }

    fun resetSaveState() = _uiState.update { it.copy(saveSuccess = false) }
}