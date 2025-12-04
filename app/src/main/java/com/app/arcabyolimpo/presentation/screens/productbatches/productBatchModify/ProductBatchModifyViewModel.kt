package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchModify

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.usecase.productbatches.GetProductBatchUseCase
import com.app.arcabyolimpo.domain.usecase.productbatches.ModifyProductBatchUseCase
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toDomain
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toUiModel
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchDetail.ProductBatchDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/**
 * ViewModel for the ProductBatchModifyScreen.
 * This class manages the state and business logic for modifying an existing product batch.
 * It fetches the batch details, handles user input, validates the data, and communicates
 * with the corresponding use cases to persist the changes.
 *
 * @param modifyProductBatchModifyUseCase The use case responsible for updating the batch data.
 * @param getProductBatchUseCase The use case for fetching a single product batch by its ID.
 */
@HiltViewModel
class ProductBatchModifyViewModel
    @Inject
    constructor(
        private val modifyProductBatchModifyUseCase: ModifyProductBatchUseCase,
        private val getProductBatchUseCase: GetProductBatchUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(ProductBatchModifyUiState())
            private set

        /**
         * Loads the details of a specific product batch to populate the modification form.
         *
         * @param id The unique identifier of the product batch to load.
         */
        fun loadBatch(id: String) {
            viewModelScope.launch {
                try {
                    val batch = getProductBatchUseCase(id).toUiModel()

                    val formattedFechaRealizacion = formatDate(batch.fechaRealizacion)
                    val formattedFechaCaducidad = batch.fechaCaducidad?.let { formatDate(it) } ?: ""

                    uiState =
                        uiState.copy(
                            precioVenta = batch.precioVenta.toString(),
                            cantidadProducida = batch.cantidadProducida.toString(),
                            fechaRealizacion = formattedFechaRealizacion,
                            fechaCaducidad = formattedFechaCaducidad,
                            error = null,
                        )
                } catch (e: Exception) {
                    uiState =
                        uiState.copy(
                            error = "No se pudo cargar el lote",
                        )
                }
            }
        }

        /**
         * Handles changes to the input fields in the UI.
         *
         * @param field A string identifier for the field that was changed (e.g., "precioVenta").
         * @param value The new value entered by the user.
         */
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

        /**
         * Validates the current form data and, if successful, triggers the modification process.
         *
         * @param id The ID of the batch being modified.
         * @param onSuccess A callback function to be executed upon successful modification,
         *                  typically used for navigation.
         */
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

        /**
         * Formats an ISO 8601 date string into a "dd/MM/yyyy" format.
         *
         * @param isoDate The date string in "yyyy-MM-dd'T'HH:mm:ss" format.
         * @return The formatted date string, or an empty string if parsing fails.
         */

        private fun formatDate(isoDate: String): String =
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val date = inputFormat.parse(isoDate)
                date?.let { outputFormat.format(it) } ?: ""
            } catch (e: Exception) {
                ""
            }

        /**
         * Checks if one date string occurs before another.
         *
         * @param finalDate The date to check.
         * @param referenceDate The date to compare against.
         * @return True if finalDate is before referenceDate or if parsing fails; otherwise, false.
         */

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

        /**
         * Executes the actual modification by calling the appropriate use case.
         *
         * @param id The ID of the batch to modify.
         * @param onSuccess The callback function to run after a successful update.
         */

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
