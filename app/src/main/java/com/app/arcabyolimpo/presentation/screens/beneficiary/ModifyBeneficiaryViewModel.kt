package com.app.arcabyolimpo.presentation.screens.beneficiary

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryFormData
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import com.app.arcabyolimpo.domain.usecase.beneficiaries.PostModifyBeneficiary
import com.app.arcabyolimpo.domain.usecase.disabilities.GetDisabilitiesUseCase
import com.app.arcabyolimpo.domain.usecase.upload.PostUploadImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * ViewModel responsible for managing the UI state of the Modify Beneficiary screen.
 *
 * This class interacts with the [GetDisabilitiesUseCase] to fetch data from the registered disabilities.
 *
 * @property GetDisabilitiesUseCase Use case for retrieving the existing list of disabilities.
 * @property PostModifyBeneficiary Use case for updating the data of the beneficiary in the database.
 */

@HiltViewModel
class ModifyBeneficiaryViewModel @Inject constructor(
    private val postModifyBeneficiary: PostModifyBeneficiary,
    private val getDisabilitiesUseCase: GetDisabilitiesUseCase,
    private val repository: BeneficiaryRepository,
    private val postUploadImage: PostUploadImage,
    @ApplicationContext private val context: Context
) : ViewModel(){

    private val _uiState = MutableStateFlow(ModifyBeneficiaryUiState())
    val uiState: StateFlow<ModifyBeneficiaryUiState> = _uiState.asStateFlow()
    private val _formData = MutableStateFlow(BeneficiaryFormData())
    val formData: StateFlow<BeneficiaryFormData> = _formData.asStateFlow()
    private val _fieldErrors = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, Boolean>> = _fieldErrors.asStateFlow()

    private val _beneficiary = MutableStateFlow<Beneficiary?>(null)

    private val _disabilities = MutableStateFlow<List<Disability>>(emptyList())
    val disabilities: StateFlow<List<Disability>> = _disabilities.asStateFlow()

    private val _workLoadError = MutableStateFlow<String?>(null)
    val workLoadError: StateFlow<String?> = _workLoadError.asStateFlow()

    private val _disability = MutableStateFlow<Disability?>(null)
    val disability: StateFlow<Disability?> = _disability.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri.asStateFlow()

    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun getFileFromUri(context: Context, uri: Uri): File? {
        return try {
            val contentResolver = context.contentResolver
            val tempFile = File(context.cacheDir, "upload_temp_${System.currentTimeMillis()}")
            contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(tempFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun loadDisabilities() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getDisabilitiesUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is Result.Success -> {
                        _disabilities.value = result.data
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al cargar discapacidades: ${result.exception.message}"
                            )
                        }
                    }
                }
            }
        }
    }
    fun loadBeneficiary(idBeneficiary: String) {
        if (idBeneficiary.isBlank()) {
            _workLoadError.value = "ID del taller no válido:" + idBeneficiary
            _isLoading.value = false
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSuccess = false) }

            val imageUri = _selectedImageUri.value
            var remoteImageUrl: String = ""
            var uploadError: String? = null

            if (imageUri != null) {
                val fileToUpload = getFileFromUri(context, imageUri)

                if (fileToUpload == null) {
                    uploadError = "Error al preparar la imagen para la subida."
                } else {
                    val uploadResult = postUploadImage(fileToUpload)
                        .let { flow ->
                            flow.first { it !is Result.Loading }
                        }

                    when (uploadResult) {
                        is Result.Success -> {
                            remoteImageUrl = uploadResult.data.url
                            fileToUpload.delete()
                        }
                        is Result.Error -> {
                            uploadError = "Error al subir la imagen: ${uploadResult.exception.message}"
                            fileToUpload.delete()
                        }
                        is Result.Loading -> { }
                    }
                }

                if (uploadError != null) {
                    _uiState.update { it.copy(isLoading = false, error = uploadError) }
                    return@launch
                }
            }

            _isLoading.value = true
            try {
                val beneficiaryData = repository.getBeneficiaryById(idBeneficiary)
                _beneficiary.value = beneficiaryData

                if (beneficiaryData != null) {
                    _formData.update {
                        it.copy(
                            nombre = beneficiaryData.firstName?: "",
                            apellidoPaterno = beneficiaryData.paternalName?: "",
                            apellidoMaterno = beneficiaryData.maternalName?: "",
                            fechaNacimiento = beneficiaryData.birthdate,
                            fechaIngreso = beneficiaryData.entryDate,
                            numeroEmergencia = beneficiaryData.emergencyNumber,
                            nombreContactoEmergencia = beneficiaryData.emergencyName,
                            relacionContactoEmergencia = beneficiaryData.emergencyRelation,
                            descripcion = beneficiaryData.details,
                            foto = remoteImageUrl,
                            disabilities = beneficiaryData.disabilities,
                        )
                    }
                } else {
                    _workLoadError.value = "No se encontró el taller"
                }
            } catch (e: Exception) {
                _workLoadError.value = "Error al cargar el taller: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    private fun formatBeneficiaryDate(dateString: String?): String {
        if (dateString.isNullOrEmpty()) {
            return ""
        }

        return try {
            val instant = Instant.parse(dateString)
            val zonedDateTime = instant.atZone(ZoneId.systemDefault())

            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            zonedDateTime.format(formatter)
        } catch (e: Exception) {

            if (dateString.matches(Regex("^\\d{4}-\\d{2}-\\d{2}"))) {
                val parts = dateString.split("-")
                "${parts[2]}/${parts[1]}/${parts[0]}"
            } else {
                Log.e("BENEFICIARY_DEBUG", "Error al formatear o validar fecha: $dateString", e)
                ""
            }
        }
    }


    fun modifyBeneficiary(idBeneficiary: String) {
        if (!validateForm()) return
        Log.d("DEBUG_SAVE", "Intentando guardar -> Nombre: ${_formData.value.nombre}")
        viewModelScope.launch {
            val fechaNacimiento = LocalDate.parse(_formData.value.fechaNacimiento, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val fechaIngreso = LocalDate.parse(_formData.value.fechaIngreso, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            val beneficiaryDto = BeneficiaryDto(
                id = idBeneficiary,
                firstName = _formData.value.nombre,
                paternalName = _formData.value.apellidoPaterno,
                maternalName = _formData.value.apellidoMaterno,
                birthdate = fechaNacimiento,
                emergencyNumber = _formData.value.numeroEmergencia,
                emergencyName = _formData.value.nombreContactoEmergencia,
                emergencyRelation = _formData.value.relacionContactoEmergencia,
                details = _formData.value.descripcion,
                entryDate = fechaIngreso,
                image = _formData.value.foto,
                disabilities = _formData.value.disabilities,
                status = 1
            )
            postModifyBeneficiary(beneficiaryDto).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(
                            isLoading = true,
                            error = null,
                            isSuccess = false
                        )

                        is Result.Success -> state.copy(
                            modifiedBeneficiary = beneficiaryDto,
                            isLoading = false,
                            error = null,
                            isSuccess = true
                        )

                        is Result.Error -> state.copy(
                            isLoading = false,
                            error = result.exception.message ?: "Error al modificar el beneficiario",
                            isSuccess = false
                        )
                    }
                }
            }
        }
    }
    fun updateFormData(update: BeneficiaryFormData.() -> BeneficiaryFormData) {
        _formData.update { it.update() }
        clearFieldErrors()
    }

    fun resetForm() {
        _formData.value = BeneficiaryFormData()
        _fieldErrors.value = emptyMap()
        _uiState.update { it.copy(isSuccess = false, error = null) }
    }

    private fun clearFieldErrors() {
        _fieldErrors.value = emptyMap()
    }

    private fun validateForm(): Boolean {
        val data = _formData.value
        val errors = mutableMapOf<String, Boolean>()
        val dateRegex = Regex("^\\d{2}/\\d{2}/\\d{4}$")

        // Data validation 1 by 1
        if (data.nombre.isBlank()) { errors["nombre"] = true }
        if (data.apellidoPaterno.isBlank()) { errors["apellidoPaterno"] = true }
        if (data.apellidoMaterno.isBlank()) { errors["apellidoMaterno"] = true }
        if (data.fechaNacimiento.isBlank()) { errors["fechaNacimiento"] = true }
        else if (!dateRegex.matches(data.fechaNacimiento)) { errors["fechaNacimiento"] = true }
        if (data.numeroEmergencia.isBlank()) { errors["numeroEmergencia"] = true }
        else if (data.numeroEmergencia.length != 10) { errors["numeroEmergencia"] = true }
        else if (!data.numeroEmergencia.all { it.isDigit() }) { errors["numeroEmergencia"] = true }
        if (data.nombreContactoEmergencia.isBlank()) { errors["nombreContactoEmergencia"] = true }
        if (data.relacionContactoEmergencia.isBlank()) { errors["relacionContactoEmergencia"] = true }
        if (data.fechaIngreso.isBlank()) { errors["fechaIngreso"] = true }
        else if (!dateRegex.matches(data.fechaIngreso)) { errors["fechaIngreso"] = true }
        if (data.disabilities.isEmpty()) { errors["discapacidad"] = true }
        if (data.descripcion.isBlank()) { errors["descripcion"] = true }
        _fieldErrors.value = errors
        return errors.isEmpty()
    }
}