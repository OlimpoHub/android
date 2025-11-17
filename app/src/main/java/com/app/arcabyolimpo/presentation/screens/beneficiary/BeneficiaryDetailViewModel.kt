package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.beneficiaries.DeleteBeneficiaryUseCase
import com.app.arcabyolimpo.domain.usecase.beneficiaries.GetBeneficiaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeneficiaryDetailViewModel
    @Inject
    constructor(
        private val getBeneficiaryUseCase: GetBeneficiaryUseCase,
        private val deleteBeneficiaryUseCase: DeleteBeneficiaryUseCase,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(BeneficiaryDetailUiState())
        val uiState: StateFlow<BeneficiaryDetailUiState> = _uiState.asStateFlow()

        private var beneficiaryId: String? = null

        init {
            beneficiaryId = savedStateHandle.get<String>("beneficiaryId")
            println("Beneficiary ID recibido: $beneficiaryId")
            if (beneficiaryId != null) {
                loadBeneficiary(beneficiaryId!!)
            } else {
                _uiState.update { it.copy(screenError = "Beneficiario no encontrado") }
            }
        }

        fun loadBeneficiary(id: String) {
            viewModelScope.launch {
                getBeneficiaryUseCase(id).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(isScreenLoading = true)
                            is Result.Success ->
                                state.copy(
                                    isScreenLoading = false,
                                    beneficiary = result.data,
                                    screenError = null,
                                )
                            is Result.Error ->
                                state.copy(
                                    isScreenLoading = false,
                                    screenError = result.exception.message,
                                )
                        }
                    }
                }
            }
        }

        fun onDeleteClicked() {
            if (beneficiaryId == null) return

            viewModelScope.launch {
                deleteBeneficiaryUseCase(beneficiaryId!!).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(isDeleting = true)
                            is Result.Success ->
                                state.copy(
                                    isDeleting = false,
                                    deleteSuccess = true,
                                    deleteError = null,
                                )
                            is Result.Error ->
                                state.copy(
                                    isDeleting = false,
                                    deleteError = result.exception.message,
                                )
                        }
                    }
                }
            }
        }

        fun onDeletionErrorShown() {
            _uiState.update { it.copy(deleteError = null) }
        }
    }
