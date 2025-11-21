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
import androidx.core.net.toUri
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
                                error = productResult.exception?.message ?: "Error al cargar insumo"
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
        _uiState.update { it.copy(name = name) }
    }
    fun onUnitaryPriceChange(unitaryPrice: String) {
        _uiState.update { it.copy(unitaryPrice = unitaryPrice) }
    }
    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUrl = uri) }
    }

    fun onStatusChange(newStatus: Int) {
        _uiState.update { it.copy(status = newStatus) }
    }
    fun onWorkshopSelected(idWorkshop: String) {
        _uiState.update { it.copy(selectedIdWorkshop = idWorkshop) }
    }
    fun onCategorySelected(idCategory: String) {
        _uiState.update { it.copy(selectedIdCategory = idCategory) }
    }

    fun onModifyClick() {
        val formData = _uiState.value

        if (!formData.hadChanged) {
            _uiState.update { it.copy(error = "No se realizó ningun cambio") }
            return
        }

        if (
            formData.selectedIdWorkshop == null
            || formData.selectedIdCategory == null
            || formData.name == null
            || formData.unitaryPrice == null
            || formData.description == null
            || formData.selectedImageUrl == null
        ) {
            _uiState.update { it.copy(error = "Ningún campo puede estar vacío") }
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