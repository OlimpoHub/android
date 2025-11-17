package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchModify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.usecase.productbatches.ModifyProductBatchUseCase
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductBatchModifyViewModel
    @Inject
    constructor(
        private val modifyProductBatchModifyUseCase: ModifyProductBatchUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(ProductBatchModifyUiState())
            private set

        fun onFieldChange(
            field: String,
            value: String,
        ) {
            when (field) {
                "precioVenta" -> uiState = uiState.copy(precioVenta = value)
                "cantidadProducida" -> uiState = uiState.copy(cantidadProducida = value)
                "fechaCaducidad" -> uiState = uiState.copy(fechaCaducidad = value)
                "fechaRealizacion" -> uiState = uiState.copy(fechaRealizacion = value)
            }
        }

        fun modify(
            id: String,
            onSuccess: () -> Unit,
        ) {
            viewModelScope.launch {
                try {
                    val newBatch = uiState.toDomain()
                    println("Arrived to ViewModel Layer, batch: $newBatch, id: $id")
                    modifyProductBatchModifyUseCase(newBatch, id)
                    onSuccess()
                } catch (e: Exception) {
                    uiState = uiState.copy(error = "Error al modificar lote")
                }
            }
        }
    }
