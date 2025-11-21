package com.app.arcabyolimpo.presentation.screens.workshop

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import com.app.arcabyolimpo.domain.usecase.workshops.DeleteWorkshopUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WorkshopDetailViewModel @Inject constructor(
    private val repository: WorkshopRepository,
    savedStateHandle: SavedStateHandle,
    //Yessica
    private val deleteWorkshopsUseCase: DeleteWorkshopUseCase,
) : ViewModel() {

    private val workshopId: String = savedStateHandle.get<String>("id") ?: ""

    private val _workshop = MutableStateFlow<Workshop?>(null)
    val workshop: StateFlow<Workshop?> = _workshop.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _formattedDate = MutableStateFlow("")
    val formattedDate: StateFlow<String> = _formattedDate.asStateFlow()

    //Yessica

    private val _uiState = MutableStateFlow(WorkshopDetailUiState())
    val uiState: StateFlow<WorkshopDetailUiState> = _uiState.asStateFlow()
    //-------

    init {
        loadWorkshop()
    }

    fun loadWorkshop() {
        if (workshopId.isBlank()) {
            _errorMessage.value = "ID del taller no válido:" + workshopId
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val workshopData = repository.getWorkshopsById(workshopId)
                _workshop.value = workshopData
                if (workshopData != null) {
                    _formattedDate.value = formatWorkshopDate(workshopData.date)
                } else {
                    _errorMessage.value = "No se encontró el taller"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar el taller: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun formatWorkshopDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) {
            return "Fecha no disponible"
        }

        return try {
            val instant = Instant.parse(dateString)
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es", "ES"))
            zonedDateTime.format(formatter)
        } catch (e: Exception) {
            dateString
        }
    }

    /**
     * Toggles the visibility of the decision dialog used to confirm
     * the deletion of a workshop.
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
     * Deletes a single workshop identified by its [id].
     *
     * This function orchestrates the deletion flow:
     * - Calls [DeleteWorkshopUseCase] with the supply ID.
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
    fun deleteWorkshops(id: String) {
        viewModelScope.launch {
            deleteWorkshopsUseCase(id).collect { result ->
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


    //--------------


}