@file:Suppress("ktlint:standard:filename")

package com.app.arcabyolimpo.data.repository.qr

import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.qr.CreateQrDto
import com.app.arcabyolimpo.domain.repository.qr.QrRepository
import okhttp3.ResponseBody
import javax.inject.Inject

// Repository implementation that interacts with the remote API to fetch user data.
// Converts the received DTOs into domain models using the user mapper before returning them.

class QrRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
    ) : QrRepository {
        override suspend fun postCreateQr(
            userID: String,
            workshopID: String,
        ): ByteArray {
            try {
                val response = api.postCreateQr(CreateQrDto(userID, workshopID))
                val pngBytes = response.bytes()
                return pngBytes
            } catch (e: Exception) {
                throw Exception("Error al crear qr: ${e.message}", e)
            }
        }
    }
