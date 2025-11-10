package com.app.arcabyolimpo.presentation.screens.supplybatchregister

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import com.app.arcabyolimpo.domain.usecase.supplies.GetSuppliesListUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.RegisterSupplyBatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplyBatchRegisterViewModel
	@Inject
	constructor(
		private val getSuppliesListUseCase: GetSuppliesListUseCase,
		private val registerSupplyBatchUseCase: RegisterSupplyBatchUseCase,
	) : ViewModel() {

	private val _uiState = MutableStateFlow(SupplyBatchRegisterUiState())
	val uiState: StateFlow<SupplyBatchRegisterUiState> = _uiState.asStateFlow()

	init {
		loadSuppliesList()
	}

	// Load supplies list using the same pattern as SuppliesListViewModel: expose isLoading / error
	fun loadSuppliesList() {
		viewModelScope.launch {
			getSuppliesListUseCase().collect { result ->
				_uiState.update { state ->
					when (result) {
						is Result.Loading -> state.copy(isLoading = true, error = null)
						is Result.Success -> state.copy(suppliesList = result.data, isLoading = false, error = null)
						is Result.Error -> state.copy(isLoading = false, error = result.exception.message)
					}
				}
			}
		}
	}

	fun onSelectSupply(id: String) {
		_uiState.update { it.copy(selectedSupplyId = id) }
	}

	fun onQuantityChanged(value: String) {
		_uiState.update { it.copy(quantityInput = value) }
	}

	fun onExpirationDateChanged(value: String) {
		_uiState.update { it.copy(expirationDateInput = value) }
	}

	fun onBoughtDateChanged(value: String) {
		_uiState.update { it.copy(boughtDateInput = value) }
	}

	fun registerBatch() {
		val current = _uiState.value

		val supplyId = current.selectedSupplyId
		val quantity = current.quantityInput.toIntOrNull()
		val expiration = current.expirationDateInput
		val bought = current.boughtDateInput

		if (supplyId.isNullOrBlank() || quantity == null) {
			_uiState.update { it.copy(registerError = "Seleccione un insumo y coloque una cantidad válida") }
			return
		}

		val batch = SupplyBatch(
			supplyId = supplyId,
			quantity = quantity,
			expirationDate = expiration,
			boughtDate = bought,
		)

		viewModelScope.launch {
			registerSupplyBatchUseCase(batch).collect { result ->
				_uiState.update { state ->
					when (result) {
						is Result.Loading -> state.copy(registerLoading = true, registerError = null, registerSuccess = false)
						is Result.Success -> state.copy(registerLoading = false, registerSuccess = true, registerError = null)
						is Result.Error -> state.copy(registerLoading = false, registerError = result.exception.message, registerSuccess = false)
					}
				}
			}
		}
	}

	fun clearRegisterStatus() {
		_uiState.update { it.copy(registerLoading = false, registerError = null, registerSuccess = false) }
	}

}