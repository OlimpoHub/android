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
import java.text.SimpleDateFormat
import java.util.Locale
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
                "precioVenta" ->
                    uiState =
                        uiState.copy(
                            precioVenta = value,
                            isPrecioVentaError = false,
                        )
                "cantidadProducida" ->
                    uiState =
                        uiState.copy(
                            cantidadProducida = value,
                            isCantidadProducidaError = false,
                        )
                "fechaCaducidad" ->
                    uiState =
                        uiState.copy(
                            fechaCaducidad = value,
                            isFechaCaducidadError = false,
                        )
                "fechaRealizacion" ->
                    uiState =
                        uiState.copy(
                            fechaRealizacion = value,
                            isFechaRealizacionError = false,
                        )
            }
        }

        fun validateAndModify(
            id: String,
            onSuccess: () -> Unit,
        ) {
            val precioVentaInvalid = uiState.precioVenta.isBlank()
            val cantidadProducidaInvalid = uiState.cantidadProducida.isBlank() || uiState.cantidadProducida == "0"
            val fechaRealizacionInvalid = uiState.fechaRealizacion.isBlank()

            var fechaCaducidadInvalid = false

            if (uiState.fechaCaducidad.isNotBlank()) {
                if (uiState.fechaRealizacion.isNotBlank()) {
                    if (isDateBefore(uiState.fechaCaducidad, uiState.fechaRealizacion)) {
                        fechaCaducidadInvalid = true
                    }
                }
            }

            uiState =
                uiState.copy(
                    isPrecioVentaError = precioVentaInvalid,
                    isCantidadProducidaError = cantidadProducidaInvalid,
                    isFechaCaducidadError = fechaCaducidadInvalid,
                    isFechaRealizacionError = fechaRealizacionInvalid,
                )

            val hasError =
                listOf(
                    precioVentaInvalid,
                    cantidadProducidaInvalid,
                    fechaCaducidadInvalid,
                    fechaRealizacionInvalid,
                ).any { it }

            if (!hasError) {
                modify(
                    id = id,
                    onSuccess = onSuccess,
                )
            }
        }

        private fun isDateBefore(
            finalDate: String,
            referenceDate: String,
        ): Boolean =
            try {
                val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val date1 = format.parse(finalDate)
                val date2 = format.parse(referenceDate)

                if (date1 != null && date2 != null) {
                    date1.before(date2)
                } else {
                    false
                }
            } catch (e: Exception) {
                true
            }

        private fun modify(
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
