package com.app.arcabyolimpo.presentation.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.user.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * [UserListViewModel] manages all UI-related data and logic for the User List screen.
 *
 * It interacts with the domain layer through [GetUsersUseCase] to fetch user data,
 * exposes UI state via [uiState], and handles user actions such as filtering,
 * clearing filters, and searching.
 *
 * This ViewModel is lifecycle-aware and scoped to the composable that uses it.
 *
 * @property getUsersUseCase Use case responsible for fetching user data from the repository.
 */

@HiltViewModel
class UserListViewModel
    @Inject
    constructor(
        private val getUsersUseCase: GetUsersUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(UserListUiState())
        val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

        fun getUsers() {
            viewModelScope.launch {
                getUsersUseCase().collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )
                            is Result.Success ->
                                state.copy(
                                    users = result.data,
                                    allUsers = result.data,
                                    searchedUsers = result.data,
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

        fun clearFilters() {
            _uiState.update { current ->
                current.copy(
                    selectedFilters = FilterDto(emptyMap(), order = "ASC"),
                    users = current.allUsers,
                    searchedUsers = current.allUsers,
                )
            }
        }

        fun applyFilters(filterDto: FilterDto) {
            _uiState.update { current ->
                val filteredUsers =
                    current.allUsers.filter { user ->
                        println(user.nombre)
                        println(user.copiaINE)
                        println(user.avisoConfidencialidad)
                        println(user.reglamentoInterno)
                        val statusFilter = filterDto.filters["Estatus"]
                        val documentFilter = filterDto.filters["Documentos entregados"]
                        val matchesStatus =
                            when {
                                statusFilter == null -> true
                                "Activo" in statusFilter && user.estatus == 1 -> true
                                "Inactivo" in statusFilter && user.estatus == 0 -> true
                                else -> false
                            }
                        val matchesDocument =
                            when {
                                documentFilter == null || documentFilter.isEmpty() -> true
                                else -> {
                                    var hasAll = true
                                    if ("Reglamento interno" in documentFilter && user.reglamentoInterno != 1) {
                                        hasAll =
                                            false
                                    }
                                    if ("Copia de INE" in documentFilter && user.copiaINE != 1) hasAll = false
                                    if ("Aviso de confidencialidad" in documentFilter && user.avisoConfidencialidad != 1) {
                                        hasAll =
                                            false
                                    }
                                    hasAll
                                }
                            }
                        matchesStatus && matchesDocument
                    }
                val filteredUsersFinal = if (filterDto.order == "ASC") filteredUsers else filteredUsers.reversed()
                current.copy(
                    selectedFilters = filterDto,
                    users = filteredUsersFinal,
                )
            }
        }

        fun searchUsers(searchInput: String) {
            _uiState.update { current ->
                val filteredUsers =
                    current.users.filter { user ->
                        val completeName = user.nombre + user.apellidoPaterno + user.apellidoMaterno
                        completeName.contains(searchInput)
                    }

                current.copy(
                    searchedUsers = filteredUsers,
                )
            }
        }
    }
