package com.app.arcabyolimpo.domain.usecase.upload

data class UploadResponse(
    val success: Boolean,
    val url: String,
    val filename: String
)