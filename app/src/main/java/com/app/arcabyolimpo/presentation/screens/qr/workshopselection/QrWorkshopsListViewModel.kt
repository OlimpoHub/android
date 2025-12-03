package com.app.arcabyolimpo.presentation.screens.qr.workshopselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.usecase.workshops.GetWorkshopsListUseCase
import com.app.arcabyolimpo.domain.usecase.workshops.SearchWorkshopsUseCase
import com.app.arcabyolimpo.presentation.screens.workshop.WorkshopsListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * ViewModel responsible for managing the UI state of the workshops list screen.
 *
 * This class interacts with the [GetWorkshopsListUseCase] to fetch data from the domain layer
 * and exposes a [StateFlow] of [WorkshopsListUiState] that the UI observes to render updates.
 *
 * @property getWorkshopsListUseCase Use case for retrieving the list of workshops.
 */
@HiltViewModel
class QrWorkshopsListViewModel
    @Inject
    constructor(
        private val getWorkshopsListUseCase: GetWorkshopsListUseCase,
        private val searchWorkshopsUseCase: SearchWorkshopsUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WorkshopsListUiState())
        val uiState: StateFlow<WorkshopsListUiState> = _uiState.asStateFlow()

        private val _searchQuery = MutableStateFlow("")
        val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

        private var originalWorkshopsList: List<Workshop> = emptyList()

        init {
            loadWorkshopsList()
            setupSearchDebounce()
        }

        fun onSearchQueryChange(text: String) {
            _searchQuery.value = text
            if (text.isBlank()) {
                _uiState.update { state ->
                    state.copy(
                        workshopsList = originalWorkshopsList,
                        isLoading = false,
                        error = null,
                    )
                }
            }
        }

        fun loadWorkshopsList() {
            viewModelScope.launch {
                getWorkshopsListUseCase().collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(isLoading = true, error = null)

                            is Result.Success -> {
                                originalWorkshopsList = result.data

                                val uniqueDates =
                                    result.data
                                        .mapNotNull { it.date?.take(10) }
                                        .distinct()
                                        .sorted()

                                state.copy(
                                    workshopsList = result.data,
                                    isLoading = false,
                                    error = null,
                                    filterData =
                                        state.filterData.copy(
                                            sections =
                                                state.filterData.sections + ("Fecha" to uniqueDates),
                                        ),
                                )
                            }

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

        private fun setupSearchDebounce() {
            viewModelScope.launch {
                _searchQuery
                    .debounce(400)
                    .distinctUntilChanged()
                    .collect { query ->
                        if (query.isNotBlank()) {
                            performSearch(query)
                        }
                    }
            }
        }

        private fun performSearch(name: String) {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }

                searchWorkshopsUseCase(name).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(isLoading = true, error = null)

                            is Result.Success ->
                                state.copy(
                                    workshopsList = result.data,
                                    isLoading = false,
                                    error = null,
                                )

                            is Result.Error -> {
                                state.copy(
                                    workshopsList = originalWorkshopsList,
                                    error = result.exception.message,
                                    isLoading = false,
                                )
                            }
                        }
                    }
                }
            }
        }

        fun clearFilters() {
            _uiState.update { current ->
                current.copy(
                    selectedFilters = FilterDto(emptyMap(), order = "ASC"),
                    workshopsList = originalWorkshopsList,
                )
            }
        }

        fun applyFilters(filterDto: FilterDto) {
            _uiState.update { current ->
                val dateFilter = getFilterValues(filterDto, "Fecha")
                val hourFilter = getFilterValues(filterDto, "Hora de Entrada")
                val statusFilter = getFilterValues(filterDto, "Estado")

                val filteredWorkshops =
                    originalWorkshopsList.filter { workshop ->

                        // ----- Fecha -----
                        val normalizedDate = (workshop.date ?: "").take(10)
                        val matchesDate =
                            when {
                                dateFilter.isNullOrEmpty() -> true
                                else ->
                                    dateFilter.any { selected ->
                                        normalizedDate == selected.take(10)
                                    }
                            }

                        // ----- Hora de entrada -----
                        val matchesHour =
                            when {
                                hourFilter.isNullOrEmpty() -> true
                                workshop.startHour.isNullOrBlank() -> false
                                else -> {
                                    val workshopHour = workshop.startHour.take(5)
                                    hourFilter.any { selected ->
                                        val selectedNorm = selected.take(5)
                                        workshopHour == selectedNorm
                                    }
                                }
                            }

                        // ----- Estatus -----
                        val matchesStatus =
                            when {
                                statusFilter.isNullOrEmpty() -> true
                                else -> {
                                    statusFilter.any { selected ->
                                        val mapped = if (selected == "Activo") 1 else 0
                                        workshop.status == mapped
                                    }
                                }
                            }

                        matchesDate && matchesHour && matchesStatus
                    }

                val filteredWorkshopsFinal =
                    if (filterDto.order == "ASC") {
                        filteredWorkshops.sortedBy { it.nameWorkshop }
                    } else {
                        filteredWorkshops.sortedByDescending { it.nameWorkshop }
                    }

                current.copy(
                    selectedFilters = filterDto,
                    workshopsList = filteredWorkshopsFinal,
                )
            }
        }

        private fun getFilterValues(
            filterDto: FilterDto,
            key: String,
        ): List<String>? {
            if (filterDto.filters.isEmpty()) return null

            val target = key.lowercase().trim()

            return filterDto.filters.entries
                .firstOrNull { (k, _) ->
                    val normalizedKey = k.lowercase().trim()
                    normalizedKey == target || normalizedKey.contains(target)
                }?.value
        }
    }
