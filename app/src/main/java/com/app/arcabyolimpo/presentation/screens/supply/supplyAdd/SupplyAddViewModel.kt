package com.app.arcabyolimpo.presentation.screens.supply.supplyAdd

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.arcabyolimpo.domain.model.supplies.SupplyAdd
import com.app.arcabyolimpo.domain.usecase.supplies.GetWorkshopCategoryInfoUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.AddSupplyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SupplyAddViewModel @Inject constructor(
    private val getWorkshopCategoryInfoUseCase: GetWorkshopCategoryInfoUseCase,
    private val addSuppliesUseCase: AddSupplyUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SupplyAddUiState())
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
    fun onUnitMeasureChange(measureUnit: String) {
        _uiState.update { it.copy(measureUnit = measureUnit) }
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
    fun onSaveClick() {
        val state = _uiState.value

        if (state.name.isBlank() || state.selectedWorkshopId == null || state.selectedCategoryId == null) {
            _uiState.update { it.copy(error = "Completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val supply = SupplyAdd(
                idWorkshop = state.selectedWorkshopId,
                name = state.name,
                measureUnit = state.measureUnit,
                idCategory = state.selectedCategoryId,
                status = state.status
            )

            val result = addSuppliesUseCase(supply, state.selectedImage)

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

