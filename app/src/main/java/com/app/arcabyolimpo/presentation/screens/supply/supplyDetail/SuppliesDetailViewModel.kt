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

    fun deleteOneSupply(id: String) {
        viewModelScope.launch {
            deleteOneSupplyUseCase(id).collect { result ->
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

    }
