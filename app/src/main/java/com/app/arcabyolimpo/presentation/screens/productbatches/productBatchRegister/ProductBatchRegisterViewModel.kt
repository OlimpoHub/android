package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.usecase.productbatches.RegisterProductBatchUseCase
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ViewModel for ProductBatchRegisterScreen.
 * @param registerProductBatchUseCase RegisterProductBatchUseCase -> use case to register a new product batch
*/
@HiltViewModel
class ProductBatchRegisterViewModel
    @Inject
    constructor(
        private val registerProductBatchUseCase: RegisterProductBatchUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(ProductBatchRegisterUiState())
            private set

        fun onFieldChange(
            field: String,
            value: String,
        ) {
            when (field) {
                "idProducto" -> uiState = uiState.copy(idProducto = value)
                "precioVenta" -> uiState = uiState.copy(precioVenta = value)
                "cantidadProducida" -> uiState = uiState.copy(cantidadProducida = value)
                "fechaCaducidad" -> uiState = uiState.copy(fechaCaducidad = value)
                "fechaRealizacion" -> uiState = uiState.copy(fechaRealizacion = value)
            }
        }

        fun register(onSuccess: () -> Unit) {
            viewModelScope.launch {
                try {
                    val newBatch = uiState.toDomain()
                    registerProductBatchUseCase(newBatch)
                    onSuccess()
                } catch (e: Exception) {
                    uiState = uiState.copy(error = "Error al registrar lote")
                }
            }
        }
    }
