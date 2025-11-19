package com.app.arcabyolimpo.presentation.screens.supply.supplyBatchList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.supplies.GetSupplyBatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupplyBatchListViewModel
	@Inject
	constructor(
		private val getSupplyBatchUseCase: GetSupplyBatchUseCase,
	) : ViewModel() {

	private val _uiState = MutableStateFlow(SupplyBatchListUiState())
	val uiState: StateFlow<SupplyBatchListUiState> = _uiState.asStateFlow()

	/**
	 * Loads the supply batch list for the given supply and optional expiration date.
	 */
	fun getSupplyBatch(expirationDate: String, idSupply: String) {
		viewModelScope.launch {
			getSupplyBatchUseCase(expirationDate, idSupply).collect { result ->
				_uiState.update { state ->
					when (result) {
						is Result.Loading -> state.copy(isLoading = true)
						is Result.Success -> state.copy(supplyBatchList = result.data, isLoading = false, error = null)
						is Result.Error -> state.copy(error = result.exception.message, isLoading = false)
					}
				}
			}
		}
	}

	fun clearState() {
		_uiState.value = SupplyBatchListUiState()
	}
}