package com.app.arcabyolimpo.presentation.screens.product

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

@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val getWorkshopCategoryInfoUseCase: GetWorkshopCategoryInfoUseCase,
    private val addProductUseCase: AddProductUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductAddUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadDropDownData()
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

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }
    fun onUnitaryPriceChange(unitaryPrice: String) {
        _uiState.update { it.copy(unitaryPrice = unitaryPrice) }
    }
    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(selectedImage = uri) }
    }
    fun onStatusChange(newStatus: Int) {
        _uiState.update { it.copy(status = newStatus) }
    }
    fun onWorkshopSelected(idWorkshop: String) {
        _uiState.update { it.copy(selectedWorkshopId = idWorkshop) }
    }
    fun onCategorySelected(idCategory: String) {
        _uiState.update { it.copy(selectedCategoryId = idCategory) }
    }
    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }
    fun onSaveClick() {
        val state = _uiState.value

        if (state.name.isBlank() || state.selectedWorkshopId == null || state.selectedCategoryId == null) {
            _uiState.update { it.copy(error = "Completa todos los campos") }
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