package com.app.arcabyolimpo.domain.repository.upload

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.upload.UploadResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UploadRepository {
    fun uploadImage(imageFile: File): Flow<Result<UploadResponse>>
}