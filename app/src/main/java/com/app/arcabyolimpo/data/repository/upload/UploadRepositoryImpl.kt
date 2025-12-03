
package com.app.arcabyolimpo.data.repository.upload

import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.upload.UploadRepository
import com.app.arcabyolimpo.domain.usecase.upload.UploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class UploadRepositoryImpl @Inject constructor(
    private val api: ArcaApi
) : UploadRepository {

    override fun uploadImage(imageFile: File): Flow<Result<UploadResponse>> = flow {
        emit(Result.Loading)

        try {
            val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

            val response = api.uploadWorkshopImage(imagePart)

            if (response.url.isNullOrBlank()) {
                emit(Result.Error(Exception("No se proporcionó una URL válida.")))
            } else {
                emit(Result.Success(response))
            }

        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e))
        }
    }.flowOn(Dispatchers.IO)
}