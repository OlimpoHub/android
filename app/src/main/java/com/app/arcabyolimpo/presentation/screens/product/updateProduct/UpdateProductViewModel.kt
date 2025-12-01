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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.app.arcabyolimpo.domain.common.Result
import kotlinx.coroutines.async

@HiltViewModel
class UpdateProductViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val getWorkshopCategoryInfoUseCase: GetWorkshopCategoryInfoUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUpdateUiState())
    val state = _uiState.asStateFlow()

    private val idProduct: String = savedStateHandle.get<String>("idProduct") ?: ""

    private val regexValidation = Regex("^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ.,:;\\-()\\/\"'\n]*$")
    private val priceValidation = Regex("^\\d{1,4}(\\.\\d{1,2})?\$")


    init {
        loadFormsData()
    }

    private fun loadFormsData() {
        viewModelScope.launch {
            val categoriesWorkshopsPromise = async { getWorkshopCategoryInfoUseCase() }

            getProductUseCase(idProduct).collect { productResult ->
                when (productResult) {
                    is com.app.arcabyolimpo.domain.common.Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is com.app.arcabyolimpo.domain.common.Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = productResult.exception?.message ?: "Error al cargar producto"
                            )
                        }
                    }
                    is Result.Success -> {
                        val product = productResult.data
                        val categoriesWorkshops = categoriesWorkshopsPromise.await()

                        if (categoriesWorkshops.isSuccess) {
                            val dropdownData = categoriesWorkshops.getOrNull()

                            if (dropdownData != null) {
                                val idWorkshop = dropdownData.workshops.find {
                                    it.name == product.workshopName
                                }?.idWorkshop
                                val idCategory = dropdownData.categories.find  {
                                    it.type == product.categoryDescription
                                }?.idCategory

                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        workshops = dropdownData.workshops,
                                        categories = dropdownData.categories,
                                    ).loadProductData(
                                        name = product.name,
                                        unitaryPrice = product.unitaryPrice,
                                        status = product.status,
                                        idWorkshop = idWorkshop,
                                        idCategory = idCategory,
                                        description = product.description,
                                        imageUrl = product.image,
                                    )
                                }
                            } else {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        error = "Error al cargar los datos"
                                    )
                                }
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error =
                                        categoriesWorkshops
                                            .exceptionOrNull()?.message
                                            ?: "Error al cargar las categorias y talleres"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                isNameError = false, nameErrorMessage = ""
            )
        }
    }
    fun onUnitaryPriceChange(unitaryPrice: String) {
        _uiState.update {
            it.copy(
                unitaryPrice = unitaryPrice,
                isUnitaryPriceError = false, unitaryPriceErrorMessage = ""
            )
        }
    }
    fun onDescriptionChange(description: String) {
        _uiState.update {
            it.copy(
                description = description,
                isDescriptionError = false, descriptionErrorMessage = ""
            )
        }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update {
            it.copy(
                selectedImageUrl = uri,
                isImageError = false, imageErrorMessage = ""
            )
        }
    }

    fun onStatusChange(newStatus: Int) {
        _uiState.update {
            it.copy(
                status = newStatus,
                isStatusError = false, statusErrorMessage = ""
            )
        }
    }
    fun onWorkshopSelected(idWorkshop: String) {
        _uiState.update {
            it.copy(
                selectedIdWorkshop = idWorkshop,
                isWorkshopError = false, workshopErrorMessage = ""
            )
        }
    }
    fun onCategorySelected(idCategory: String) {
        _uiState.update {
            it.copy(
                selectedIdCategory = idCategory,
                isCategoryError = false, categoryErrorMessage = ""
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

        // 1. Validación de Nombre
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

        // 2. Validación de Imagen
        if(state.selectedImageUrl == null) {
            _uiState.update { it.copy(isImageError = true, imageErrorMessage = "Debes seleccionar una imagen.") }
            isValid = false
        }

        // 3. Validación de Precio Unitario
        if(state.unitaryPrice.isBlank()) {
            _uiState.update { it.copy(isUnitaryPriceError = true, unitaryPriceErrorMessage = "El precio unitario no puede estar vacío.") }
            isValid = false
        } else if(!state.unitaryPrice.matches(priceValidation)) {
            _uiState.update { it.copy(isUnitaryPriceError = true, unitaryPriceErrorMessage = "Ingresa un precio válido (ej: 10.50 o 10).") }
            isValid = false
        }

        // 4. Validación de Taller
        if(state.selectedIdWorkshop.isNullOrBlank()) {
            _uiState.update { it.copy(isWorkshopError = true, workshopErrorMessage = "Debes seleccionar un taller.") }
            isValid = false
        }

        // 5. Validación de Categoría
        if(state.selectedIdCategory.isNullOrBlank()) {
            _uiState.update { it.copy(isCategoryError = true, categoryErrorMessage = "Debes seleccionar una categoría.") }
            isValid = false
        }

        // 6. Validación de Estatus
        if(state.status == 0) {
            _uiState.update { it.copy(isStatusError = true, statusErrorMessage = "El estatus seleccionado es inválido (no puede ser 0).") }
            isValid = false
        }

        // 7. Validación de Descripción
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

    fun onModifyClick() {
        val formData = _uiState.value

        if (!formData.hadChanged) {
            _uiState.update { it.copy(error = "No se realizó ningun cambio") }
            return
        }

        if (!isValidFields()) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val product = ProductUpdate(
                name = formData.name,
                idWorkshop = formData.selectedIdWorkshop,
                unitaryPrice = formData.unitaryPrice,
                idCategory = formData.selectedIdCategory,
                description = formData.description,
                status = formData.status.toString(),
                image = formData.selectedImageUrl
            )

            val result = updateProductUseCase(
                id = idProduct,
                productData = product
            )

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        isSaving = false,
                        success = true
                    )
                } else {
                    it.copy(
                        isSaving = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }

}