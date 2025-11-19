package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.product.GetProductsListUseCase
import com.app.arcabyolimpo.domain.usecase.productbatches.GetProductBatchesUseCase
import com.app.arcabyolimpo.domain.usecase.productbatches.RegisterProductBatchUseCase
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toDomain
import com.app.arcabyolimpo.presentation.screens.productbatches.mapper.toUiModel
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList.ProductBatchesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

/** ViewModel for ProductBatchRegisterScreen.
 * @param registerProductBatchUseCase RegisterProductBatchUseCase -> use case to register a new product batch
*/
@HiltViewModel
class ProductBatchRegisterViewModel
    @Inject
    constructor(
        private val registerProductBatchUseCase: RegisterProductBatchUseCase,
        private val getProductsListUseCase: GetProductsListUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(ProductBatchRegisterUiState())
            private set

        fun loadData() {
            viewModelScope.launch {
                getProductsListUseCase().collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            uiState = uiState.copy(isLoadingProducts = true)
                        }
                        is Result.Success -> {
                            val products = result.data

                            val ids = products.map { it.id }
                            val names = products.map { it.name }

                            uiState =
                                uiState.copy(
                                    productIds = ids,
                                    names = names,
                                    isLoadingProducts = false,
                                )
                        }
                        is Result.Error -> {
                            uiState =
                                uiState.copy(
                                    isLoadingProducts = false,
                                    error = result.exception.message ?: "Error al cargar productos",
                                )
                        }
                    }
                }
            }
        }

        fun onFieldChange(
            field: String,
            value: String,
        ) {
            when (field) {
                "idProducto" ->
                    uiState =
                        uiState.copy(
                            idProducto = value,
                            isIdProductoError = false,
                        )
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

        fun validateAndRegister(onSuccess: () -> Unit) {
            val idProductoInvalid = uiState.idProducto.isBlank()
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
                    isIdProductoError = idProductoInvalid,
                    isPrecioVentaError = precioVentaInvalid,
                    isCantidadProducidaError = cantidadProducidaInvalid,
                    isFechaCaducidadError = fechaCaducidadInvalid,
                    isFechaRealizacionError = fechaRealizacionInvalid,
                )

            val hasError =
                listOf(
                    idProductoInvalid,
                    precioVentaInvalid,
                    cantidadProducidaInvalid,
                    fechaCaducidadInvalid,
                    fechaRealizacionInvalid,
                ).any { it }

            if (!hasError) {
                register(onSuccess)
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

        private fun register(onSuccess: () -> Unit) {
            viewModelScope.launch {
                try {
                    val newBatch = uiState.toDomain()
                    registerProductBatchUseCase(newBatch)
                    onSuccess()
                } catch (e: Exception) {
                    uiState = uiState.copy(error = "Error al registrar lote: ${e.message}")
                }
            }
        }
    }
