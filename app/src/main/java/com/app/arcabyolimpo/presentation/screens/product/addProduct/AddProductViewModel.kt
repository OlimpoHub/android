package com.app.arcabyolimpo.presentation.screens.product.addProduct

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.usecase.product.AddProductUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetWorkshopCategoryInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic of the 'Add Product' screen.
 *
 * It handles loading necessary data (workshops, categories), capturing user input,
 * validating the data, and triggering the use case to save the new product.
 *
 * @property getWorkshopCategoryInfoUseCase Use case to fetch lists for dropdown menus.
 * @property addProductUseCase Use case to save the new product data to the repository.
 */
@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val getWorkshopCategoryInfoUseCase: GetWorkshopCategoryInfoUseCase,
    private val addProductUseCase: AddProductUseCase,
) : ViewModel() {
    private var _uiState = MutableStateFlow(ProductAddUiState())

    /**
     * Exposes the current [ProductAddUiState] as a read-only StateFlow for the UI to observe.
     */
    val uiState = _uiState.asStateFlow()
    private val regexValidation = Regex("^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ.,:;\\-()\\/\"'\n]*$")
    private val priceValidation = Regex("^(\\d*\\.)?\\d+$")

    init {
        loadDropDownData()
    }

    /**
     * Fetches the list of available workshops and categories needed for the dropdown menus.
     * Updates the [_uiState] with the loaded data or an error message.
     */
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

    // --------------------------- Input Handlers ---------------------------
    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                isNameError = false,
            )
        }
    }
    fun onUnitaryPriceChange(unitaryPrice: String) {
        _uiState.update {
            it.copy(
                unitaryPrice = unitaryPrice,
                isUnitaryPriceError = false,
            )
        }

    }
    fun onImageSelected(uri: Uri?) {
        _uiState.update {
            it.copy(
                selectedImage = uri,
                isImageError = false,
            )
        }
    }
    fun onStatusChange(newStatus: Int) {
        _uiState.update {
            it.copy(
                status = newStatus,
                isStatusError = false,
            )
        }
    }
    fun onWorkshopSelected(idWorkshop: String) {
        _uiState.update {
            it.copy(
                selectedWorkshopId = idWorkshop,
                isWorkshopError = false,
            )
        }
    }
    fun onCategorySelected(idCategory: String) {
        _uiState.update {
            it.copy(
                selectedCategoryId = idCategory,
                isCategoryError = false,
            )
        }
    }
    fun onDescriptionChange(description: String) {
        _uiState.update {
            it.copy(
                description = description,
                isDescriptionError = false,
            )
        }
    }

    fun isValidFields() : Boolean {
        _uiState.update {
            it.copy(
                isNameError = false, nameErrorMessage = "",
                isUnitaryPriceError = false, unitaryPriceErrorMessage = "",
                isDescriptionError = false, descriptionErrorMessage = "",
                isWorkshopError = false, workshopErrorMessage = "",
                isCategoryError = false, categoryErrorMessage = "",
                isImageError = false, imageErrorMessage = "",
                isStatusError = false, statusErrorMessage = "",
            )
        }

        val state = _uiState.value
        var isValid = true

        // 1. Validacion for Name
        if (state.name.isBlank()) {
            _uiState.update { it.copy(isNameError = true, nameErrorMessage = "El nombre no puede estar vacío.") }
            isValid = false
        } else if (!state.name.matches(regexValidation)) {
            _uiState.update { it.copy(isNameError = true, nameErrorMessage = "El nombre contiene caracteres inválidos.") }
            isValid = false
        } else if (state.name.length > 35) {
            _uiState.update { it.copy(isNameError = true, nameErrorMessage = "El nombre no puede exceder los 35 caracteres.") }
            isValid = false
        }

        // 2. Validacion for image
        if(state.selectedImage == null) {
            _uiState.update { it.copy(isImageError = true, imageErrorMessage = "Debes seleccionar una imagen.") }
            isValid = false
        }

        // 3. Validación for unitary price
        if(state.unitaryPrice.isBlank()) {
            _uiState.update { it.copy(isUnitaryPriceError = true, unitaryPriceErrorMessage = "El precio unitario no puede estar vacío.") }
            isValid = false
        } else if(!state.unitaryPrice.matches(priceValidation)) {
            _uiState.update { it.copy(isUnitaryPriceError = true, unitaryPriceErrorMessage = "Ingresa un precio válido (ej: 10.50 o 10).") }
            isValid = false
        }

        // 4. Validacion for workshop
        if(state.selectedWorkshopId.isBlank()) {
            _uiState.update { it.copy(isWorkshopError = true, workshopErrorMessage = "Debes seleccionar un taller.") }
            isValid = false
        }

        // 5. Validación for category
        if(state.selectedCategoryId.isBlank()) {
            _uiState.update { it.copy(isCategoryError = true, categoryErrorMessage = "Debes seleccionar una categoría.") }
            isValid = false
        }

        // 6. Validación for status
        if(state.status == 0) {
            _uiState.update { it.copy(isStatusError = true, statusErrorMessage = "El estatus seleccionado es inválido (no puede ser 0).") }
            isValid = false
        }

        // 7. Validación for description
        if(state.description.isBlank()) {
            _uiState.update { it.copy(isDescriptionError = true, descriptionErrorMessage = "La descripción no puede estar vacía.") }
            isValid = false
        } else if (state.description.length > 401) {
            _uiState.update { it.copy(isDescriptionError = true, descriptionErrorMessage = "La descripción no puede exceder los 400 caracteres.") }
            isValid = false
        } else if (!state.description.matches(regexValidation)) {
            _uiState.update { it.copy(isDescriptionError = true, descriptionErrorMessage = "La descripción contiene caracteres inválidos.") }
            isValid = false
        }

        return isValid
    }

    /**
     * Handles the click event for the 'Save' button.
     *
     * Performs final validation and, if successful, creates a [com.app.arcabyolimpo.domain.model.product.ProductAdd] object
     * and calls the [addProductUseCase] to save the product.
     * Updates the UI state with [isLoading], [success], or [error] status.
     */
    fun onSaveClick() {
        val state = _uiState.value

        if(!isValidFields()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val product = ProductAdd(
                idWorkshop = state.selectedWorkshopId,
                name = state.name,
                unitaryPrice = state.unitaryPrice,
                idCategory = state.selectedCategoryId,
                status = state.status.toString(),
                description = state.description,
                image = state.selectedImage!!
            )

            val result = addProductUseCase(product)

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        isLoading = false,
                        success = true,
                    )
                } else {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }
}