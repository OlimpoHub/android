package com.app.arcabyolimpo.presentation.screens.capacitations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.usecase.disabilities.GetDisabilitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DisabilitiesListViewModel
@Inject
constructor(
    private val getDisabilitiesUseCase: GetDisabilitiesUseCase,
) : ViewModel() {

    private val _disabilities = MutableStateFlow<List<Disability>>(emptyList())
    private val _searchText = MutableStateFlow("")
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    val uiState: StateFlow<DisabilitiesListUiState> =
        combine(
            _disabilities,
            _searchText,
            _isLoading,
            _error,
        ) { disabilities, text, isLoading, error ->
            DisabilitiesListUiState(
                isLoading = isLoading,
                disabilities = disabilities.filter {
                    text.isBlank() || it.name.contains(text, ignoreCase = true)
                },
                error = error,
                searchText = text,
            )
        }.stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = DisabilitiesListUiState(),
        )

    init {
        getDisabilities()

        _searchText
            .drop(1)
            .debounce(350L)
            .distinctUntilChanged()
            .launchIn(viewModelScope)
    }

    fun getDisabilities() {
        getDisabilitiesUseCase()
            .onEach { result ->
                when (result) {
                    is Result.Loading -> {
                        _isLoading.update { true }
                        _error.update { null }
                    }

                    is Result.Success -> {
                        _isLoading.update { false }
                        _disabilities.update { result.data ?: emptyList() }
                    }

                    is Result.Error -> {
                        _isLoading.update { false }
                        _error.update { result.exception?.message ?: "Unknown error" }
                    }
                }
            }.launchIn(viewModelScope)
    }

    fun onSearchTextChange(text: String) {
        _searchText.update { text }
    }
}