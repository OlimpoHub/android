package com.app.arcabyolimpo.presentation.screens.supply.supplyUpdate

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.usecase.supplies.GetSupplyBatchListUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetWorkshopCategoryInfoUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.UpdateOneSupplyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.SupplyAdd
import kotlinx.coroutines.async

@HiltViewModel
class SupplyUpdateViewModel @Inject constructor(
    private val getWorkshopCategoryInfoUseCase: GetWorkshopCategoryInfoUseCase,
    private val getSupplyBatchListUseCase: GetSupplyBatchListUseCase,
    private val updateOneSupplyUseCase: UpdateOneSupplyUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val _uiState = MutableStateFlow(SupplyUpdateUiState())
    val uiState = _uiState.asStateFlow()

    private val idSupply: String = checkNotNull(savedStateHandle["idSupply"])

    init {
        loadFormsData()
    }

    private fun loadFormsData() {
        viewModelScope.launch {
            val categoriesWorkshopsPromise = async { getWorkshopCategoryInfoUseCase() }

            getSupplyBatchListUseCase(idSupply).collect { supplyResult ->
                when (supplyResult) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = supplyResult.exception?.message ?: "Error al cargar insumo"
                            )
                        }
                    }
                    is Result.Success -> {
                        val supply = supplyResult.data
                        val categoriesWorkshops = categoriesWorkshopsPromise.await()

                        if (categoriesWorkshops.isSuccess) {
                            val dropdownData = categoriesWorkshops.getOrNull()

                            if (dropdownData != null) {
                                val idWorkshop = dropdownData.workshops.find {
                                    it.name == supply.workshop
                                }?.idWorkshop
                                val idCategory = dropdownData.categories.find  {
                                    it.type == supply.category
                                }?.idCategory

                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        workshops = dropdownData.workshops,
                                        categories = dropdownData.categories,
                                    ).loadSupplyData(
                                        name = supply.name,
                                        measureUnit = supply.unitMeasure,
                                        status = supply.status,
                                        idWorkshop = idWorkshop,
                                        idCategory = idCategory,
                                        imageUrl = supply.imageUrl,
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
    fun onUnitMeasureChange(measureUnit: String) {
        _uiState.update { it.copy(measureUnit = measureUnit) }
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

        if (formData.selectedIdWorkshop == null || formData.selectedIdCategory == null) {
            _uiState.update { it.copy(error = "El taller y categoría no pueden estar vacios") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val supply = SupplyAdd(
                name = formData.name,
                idWorkshop = formData.selectedIdWorkshop,
                measureUnit = formData.measureUnit,
                idCategory = formData.selectedIdCategory,
                status = formData.status,
            )

            val result = updateOneSupplyUseCase(
                idSupply = idSupply,
                supply = supply,
                image = formData.selectedImageUrl
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