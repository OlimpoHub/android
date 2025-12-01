package com.app.arcabyolimpo.presentation.screens.product.addProduct

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.model.product.ProductAdd
import com.app.arcabyolimpo.domain.usecase.product.AddProductUseCase
import com.app.arcabyolimpo.domain.usecase.supplies.GetWorkshopCategoryInfoUseCase
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
import javax.inject.Inject

/**
 * ViewModel responsible for managing the state and business logic of the 'Add Product' screen.
 *
 * It handles loading necessary data (workshops, categories), capturing user input,
 * validating the data, and triggering the use case to save the new product.
 *
 * @property getWorkshopCategoryInfoUseCase Use case to fetch lists for dropdown menus.
 * @property addProductUseCase Use case to save the new product data to the repository.
 */
@HiltViewModel
class AddProductViewModel @Inject constructor(
    private val getWorkshopCategoryInfoUseCase: GetWorkshopCategoryInfoUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val postUploadImage: PostUploadImage,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductAddUiState())

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

    /**
     * Exposes the current [ProductAddUiState] as a read-only StateFlow for the UI to observe.
     */
    val uiState = _uiState.asStateFlow()

    init {
        loadDropDownData()
    }

    /**
     * Fetches the list of available workshops and categories needed for the dropdown menus.
     * Updates the [_uiState] with the loaded data or an error message.
     */
    private fun loadDropDownData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getWorkshopCategoryInfoUseCase()

            _uiState.update {
                if (result.isSuccess) {
                    val data = result.getOrNull()
                    it.copy(
                        isLoading = false,
                        workshops = data?.workshops ?: emptyList(),
                        categories = data?.categories ?: emptyList(),
                    )
                }else {
                    it.copy(
                        isLoading = false,
                        error = "Error cargando datos",
                    )
                }
            }
        }
    }

    // --------------------------- Input Handlers ---------------------------
    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }
    fun onUnitaryPriceChange(unitaryPrice: String) {
        _uiState.update { it.copy(unitaryPrice = unitaryPrice) }
    }
    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(selectedImage = uri) }
    }
    fun onStatusChange(newStatus: Int) {
        _uiState.update { it.copy(status = newStatus) }
    }
    fun onWorkshopSelected(idWorkshop: String) {
        _uiState.update { it.copy(selectedWorkshopId = idWorkshop) }
    }
    fun onCategorySelected(idCategory: String) {
        _uiState.update { it.copy(selectedCategoryId = idCategory) }
    }
    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    /**
     * Handles the click event for the 'Save' button.
     *
     * Performs final validation and, if successful, creates a [com.app.arcabyolimpo.domain.model.product.ProductAdd] object
     * and calls the [addProductUseCase] to save the product.
     * Updates the UI state with [isLoading], [success], or [error] status.
     */
    fun onSaveClick() {
        val state = _uiState.value

        if (
            state.name.isBlank()
            || state.unitaryPrice.isNullOrBlank()
            || state.description.isNullOrBlank()
            || state.status == null
            || state.selectedWorkshopId.isNullOrBlank()
            || state.selectedCategoryId.isNullOrBlank()
            || state.selectedImage == null
        ) {
            _uiState.update { it.copy(error = "Completa todos los campos", isLoading = false) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val imageUri = state.selectedImage
            var remoteImageUrl: String? = null
            var uploadError: String? = null

            if (imageUri != null) {
                val fileToUpload = getFileFromUri(context, imageUri)

                if (fileToUpload == null) {
                    uploadError = "Error al preparar la imagen para la subida."
                } else {
                     val uploadResult = postUploadImage(fileToUpload)
                        .let { flow ->
                            flow.first { it !is com.app.arcabyolimpo.domain.common.Result.Loading }
                        }

                    when (uploadResult) {
                        is com.app.arcabyolimpo.domain.common.Result.Success -> {
                            remoteImageUrl = uploadResult.data.url
                            fileToUpload.delete()
                        }
                        is com.app.arcabyolimpo.domain.common.Result.Error -> {
                            uploadError = "Error al subir la imagen: ${uploadResult.exception.message}"
                            fileToUpload.delete()
                        }
                        is com.app.arcabyolimpo.domain.common.Result.Loading -> { }
                    }
                }

                if (uploadError != null) {
                    _uiState.update { it.copy(isLoading = false, error = uploadError) }
                    return@launch
                }
            }

            val product = ProductAdd(
                idWorkshop = state.selectedWorkshopId,
                name = state.name,
                unitaryPrice = state.unitaryPrice,
                idCategory = state.selectedCategoryId,
                status = state.status.toString(),
                description = state.description,
                image = remoteImageUrl ?: ""
            )

            val result = addProductUseCase(product)

            _uiState.update {
                if (result.isSuccess) {
                    it.copy(
                        isLoading = false,
                        success = true,
                    )
                } else {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.message
                    )
                }
            }
        }
    }
}