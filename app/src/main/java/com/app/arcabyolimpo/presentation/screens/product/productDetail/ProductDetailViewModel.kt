package com.app.arcabyolimpo.presentation.screens.product.productDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.product.DeleteProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val deleteProductUseCase: DeleteProductUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ProductDetailUiState> =
        MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    fun toggleDecisionDialog(show: Boolean) {
        _uiState.update { it.copy(decisionDialogVisible = show) }
    }

    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarVisible = false) }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            deleteProductUseCase(id).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading ->
                            state.copy(isLoading = true)

                        is Result.Success ->
                            state.copy(
                                isLoading = false,
                                error = null,
                                decisionDialogVisible = false,
                                snackbarVisible = true,
                            )

                        is Result.Error ->
                            state.copy(
                                isLoading = false,
                                error = result.exception.message,
                                decisionDialogVisible = false,
                                snackbarVisible = true,
                            )
                    }
                }
            }
        }
    }
}
