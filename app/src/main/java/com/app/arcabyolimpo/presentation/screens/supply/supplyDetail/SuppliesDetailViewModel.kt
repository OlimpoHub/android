package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.supplies.DeleteOneSupplyUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.DeleteSupplyBatchUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetSupplyBatchListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ----------------------------------------------------------------------------------------------- *
* SuppliesDetailViewModel -> In charge of getting one Supply and its batches, and update the views
* status, recives the information from the UseCase
*
* @param getSupplyBatchListUseCase -> US where the data form a supply and its batches are fetched
* @param deleteSupplyBatchUseCase  -> Use case to delete a specific supply batch by ID.
* ----------------------------------------------------------------------------------------------- */
@HiltViewModel
class SuppliesDetailViewModel
    @Inject
    constructor(
        private val getSupplyBatchListUseCase: GetSupplyBatchListUseCase,
        private val deleteSupplyBatchUseCase: DeleteSupplyBatchUseCase,
        private val deleteOneSupplyUseCase: DeleteOneSupplyUseCase
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(SuppliesDetailUiState())
        val uiState: StateFlow<SuppliesDetailUiState> = _uiState.asStateFlow()

        private var currentSupplyId: String? = null
        var selectedBatchId: String? = null
        var selectedBatchExpirationDate: String? = null

    /** ------------------------------------------------------------------------------------------ *
     * getSupply -> receives the id of a supply then uses the US and updates the status of the views
     * marking it as loading, error or success.
     *
     * @param id: String -> ID of the supply
     * ------------------------------------------------------------------------------------------ */
    fun getSupply(id: String) {
        currentSupplyId = id
            viewModelScope.launch {
                getSupplyBatchListUseCase(id).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )
                            is Result.Success ->
                                state.copy(
                                    supplyBatchList = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            is Result.Error ->
                                state.copy(
                                    error = result.exception.message,
                                    isLoading = false,
                                )
                        }
                    }
                }
            }
        }

        fun deleteSupplyBatch(expirationDate: String) {
            viewModelScope.launch {
                currentSupplyId?.let { supplyId ->
                    deleteSupplyBatchUseCase(supplyId, expirationDate).collect { result ->
                        _uiState.update { state ->
                            when (result) {
                                is Result.Loading -> state.copy(isLoading = true)
                                is Result.Success -> {
                                    getSupply(supplyId)
                                    state.copy(
                                        decisionDialogVisible = false,
                                        snackbarVisible = true,
                                        snackbarMessage = "Lote eliminado correctamente",
                                        isLoading = false,
                                        error = null
                                    )
                                }
                                is Result.Error -> state.copy(
                                    decisionDialogVisible = false,
                                    snackbarVisible = true,
                                    snackbarMessage = "Error al eliminar lote",
                                    error = result.exception.message,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
            }
        }

        /**
         * Toggles the visibility of the decision dialog used to confirm
         * the deletion of a supply.
         *
         * This function simply updates the UI state to show
         * or hide the confirmation dialog, without yet executing the deletion logic.
         *
         * @param showdecisionDialog `true` to show the dialog,
         *                           `false` to hide it.
         */
        fun toggledecisionDialog(showdecisionDialog: Boolean, deletionType: DeletionType? = null) {
            viewModelScope.launch {
                _uiState.update {state ->
                    state.copy(
                        decisionDialogVisible = showdecisionDialog,
                        deletionType = if (showdecisionDialog) deletionType else null
                    )
                }
            }
        }

        fun confirmDeletion() {
            viewModelScope.launch {
                when (_uiState.value.deletionType) {
                    DeletionType.SUPPLY -> {
                        currentSupplyId?.let {
                            deleteOneSupply(it)
                        }
                    }
                    DeletionType.BATCH -> {
                        selectedBatchExpirationDate?.let { fecha ->
                            deleteSupplyBatch(fecha)
                        }
                    }
                    null -> {
                    }
                }
            }
        }

        /**
         * Callback that is invoked when the snackbar has already been displayed
         * and consumed by the UI.
         *
         * Its purpose is to clear the [snackbarVisible] flag to
         * prevent the snackbar from being displayed again
         *
         */
        fun onSnackbarShown() {
            _uiState.update { state ->
                state.copy(
                    snackbarVisible = false,
                    snackbarMessage = null
                )
            }
        }

        /**
         * Deletes a single supply identified by its [id].
         *
         * This function orchestrates the deletion flow:
         * - Calls [DeleteOneSupplyUseCase] with the supply ID.
         * - Listens for the [Result] emitted by the use case and updates the
         *   UI state accordingly (loading, success, error).
         *
         * Behavior by state:
         * - [Result.Loading]: activates the loading indicator.
         * - [Result.Success]:
         *      - closes the decision dialog,
         *      - displays the confirmation snackbar,
         *      - clears any previous errors.
         * - [Result.Error]:
         *      - closes the decision dialog,
         *      - displays the error snackbar,
         *      - saves the error message to the state.
         *
         * @param id ID of the supply to be deleted.
         */
        fun deleteOneSupply(id: String) {
            viewModelScope.launch {
                deleteOneSupplyUseCase(id).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(isLoading = true)

                            is Result.Success ->
                                state.copy(
                                    decisionDialogVisible = false,
                                    snackbarVisible = true,
                                    snackbarMessage = "Insumo eliminado correctamente",
                                    isLoading = false,
                                    error = null,
                                )

                            is Result.Error ->
                                state.copy(
                                    decisionDialogVisible = false,
                                    snackbarVisible = true,
                                    snackbarMessage = "Error al eliminar insumo",
                                    error = result.exception.message,
                                    isLoading = false,
                                )
                        }
                    }
                }
            }
        }

    }
