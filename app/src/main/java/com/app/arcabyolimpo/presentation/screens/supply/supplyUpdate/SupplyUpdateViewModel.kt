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
/** ---------------------------------------------------------------------------------------------- *
 * SuppliesUpdateViewModel -> Collects the information for the workshops and categories, renders the
 * previous supply information and waits for the new user information then collects it to create a
 * new Supply
 *
 * @param getWorkshopCategoryInfoUseCase -> fetch all categories and workshops
 * @param getSupplyBatchListUseCase -> gathers the supply information
 * @param updateOneSupplyUseCase -> sends the updated information of the supply
 * @return ViewModel
 * ---------------------------------------------------------------------------------------------- */
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
    private val regexValidation = Regex("^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]*$")

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
        if (name.length > 35) {
            return
        }
        if (name.matches(regexValidation)) {
            _uiState.update {
                it.copy(
                    name = name,
                    nameError = null
                )
            }
        }
    }
    fun onUnitMeasureChange(measureUnit: String) {
        if (measureUnit.length > 25) {
            return
        }

        if (measureUnit.matches(regexValidation)) {
            _uiState.update {
                it.copy(
                    measureUnit = measureUnit,
                    measureUnitError = null
                )
            }
        }
    }
    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(selectedImageUrl = uri) }
    }
    fun onStatusChange(newStatus: Int) {
        _uiState.update { it.copy(status = newStatus) }
    }
    fun onWorkshopSelected(idWorkshop: String) {
        _uiState.update {
            it.copy(
                selectedIdWorkshop = idWorkshop,
                noWorkshop = null
            )
        }
    }
    fun onCategorySelected(idCategory: String) {
        _uiState.update {
            it.copy(
                selectedIdCategory = idCategory,
                noCategory = null
            )
        }
    }

    fun onModifyClick() {
        val formData = _uiState.value
        var hasError = false

        if (!formData.hadChanged) {
            _uiState.update { it.copy(error = "No se realizó ningun cambio") }
            return
        }

        if (formData.selectedIdWorkshop == null) {
            _uiState.update { it.copy(noWorkshop = "El taller no puede estar vacio") }
            hasError = true
        }

        if (formData.selectedIdCategory == null) {
            _uiState.update { it.copy(noCategory = "La categoría no puede estar vacio") }
            hasError = true
        }

        if (formData.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Completa el nombre") }
            hasError = true
        }

        if (formData.measureUnit.isBlank()) {
            _uiState.update { it.copy(measureUnitError = "Completa el nombre") }
            hasError = true
        }

        if (hasError) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val supply = SupplyAdd(
                name = formData.name,
                idWorkshop = formData.selectedIdWorkshop!!,
                measureUnit = formData.measureUnit,
                idCategory = formData.selectedIdCategory!!,
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