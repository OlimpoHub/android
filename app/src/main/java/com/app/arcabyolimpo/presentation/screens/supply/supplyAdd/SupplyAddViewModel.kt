package com.app.arcabyolimpo.presentation.screens.supply.supplyAdd

import android.health.connect.datatypes.units.Length
import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

/** ----------------------------------------------------------------------------------------------- *
 * SuppliesAddViewModel -> Collects the information for the workshops and categories, then collects
 * the information sent by the user to create a new Supply
 *
 * @param getWorkshopCategoryInfoUseCase -> fetch all categories and workshops
 * @param addSuppliesUseCase -> in charge of sending the new supply to create it
 * @return ViewModel
 * ----------------------------------------------------------------------------------------------- */
@HiltViewModel
class SupplyAddViewModel @Inject constructor(
    private val getWorkshopCategoryInfoUseCase: GetWorkshopCategoryInfoUseCase,
    private val addSuppliesUseCase: AddSupplyUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SupplyAddUiState())
    val uiState = _uiState.asStateFlow()

    private val regexValidation = Regex("^[a-zA-Z0-9 áéíóúÁÉÍÓÚñÑ]*$")

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
        if (name.length > 50){
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

        if(measureUnit.matches(regexValidation)) {
            _uiState.update {
                it.copy(
                    measureUnit = measureUnit,
                    measureUnitError = null
                )
            }
        }
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
        var hasError = false

        if (state.name.isBlank()) {
            _uiState.update { it.copy(nameError = "Completa el nombre") }
            hasError = true
        }

        if (state.selectedWorkshopId == null) {
            _uiState.update { it.copy(noWorkshop = "Es necesario tener el taller") }
            hasError = true
        }

        if (state.selectedCategoryId == null) {
            _uiState.update { it.copy(noCategory = "Es necesaria una categoria") }
            hasError = true
        }

        if (state.measureUnit.isBlank()) {
            _uiState.update { it.copy(measureUnitError = "Completa la unidad de medida") }
            hasError = true
        }

        if(hasError) {
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }

            val supply = SupplyAdd(
                idWorkshop = state.selectedWorkshopId!!,
                name = state.name,
                measureUnit = state.measureUnit,
                idCategory = state.selectedCategoryId!!,
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

