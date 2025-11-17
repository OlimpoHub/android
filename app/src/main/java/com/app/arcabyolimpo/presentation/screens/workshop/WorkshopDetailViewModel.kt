package com.app.arcabyolimpo.presentation.screens.workshop

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WorkshopDetailViewModel @Inject constructor(
    private val repository: WorkshopRepository,
    savedStateHandle: SavedStateHandle
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

    init {
        loadWorkshop()
    }

    private fun loadWorkshop() {
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
}