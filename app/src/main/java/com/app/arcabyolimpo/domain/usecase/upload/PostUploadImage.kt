package com.app.arcabyolimpo.domain.usecase.upload

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.upload.UploadRepository
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class PostUploadImage @Inject constructor(
    private val repository: UploadRepository
) {
    operator fun invoke(imageFile: File): Flow<Result<UploadResponse>> {
        return repository.uploadImage(imageFile)
    }
}