package com.app.arcabyolimpo.domain.repository.qr

interface QrRepository {
    suspend fun postCreateQr(
        userID: String,
        workshopID: String,
    ): ByteArray
}
