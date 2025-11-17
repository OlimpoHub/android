package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

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

    /** ------------------------------------------------------------------------------------------ *
     * getSupply -> receives the id of a supply then uses the US and updates the status of the views
     * marking it as loading, error or success.
     *
     * @param id: String -> ID of the supply
     * ------------------------------------------------------------------------------------------ */
    fun getSupply(id: String) {
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
        /** ------------------------------------------------------------------------------------------ *
         * deleteSupplyBatch -> Deletes a specific supply batch using its ID. The function updates the
         * UI state to reflect loading, success, or error states.
         *
         * After successful deletion, you can optionally trigger a refresh of the supply list
         * to reflect the latest state.
         *
         * @param id: String -> ID of the supply batch to delete.
         * ------------------------------------------------------------------------------------------ */

        fun deleteSupplyBatch(id: String) {
            viewModelScope.launch {
                deleteSupplyBatchUseCase(id).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(isLoading = true)

                            is Result.Success ->
                                state.copy(
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
        fun toggledecisionDialog(showdecisionDialog: Boolean) {
            viewModelScope.launch {
                _uiState.update {state ->
                    state.copy(
                        decisionDialogVisible = showdecisionDialog,
                    )
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
                                    isLoading = false,
                                    error = null,
                                )

                            is Result.Error ->
                                state.copy(
                                    decisionDialogVisible = false,
                                    snackbarVisible = true,
                                    error = result.exception.message,
                                    isLoading = false,
                                )
                        }
                    }
                }
            }
        }
    }
