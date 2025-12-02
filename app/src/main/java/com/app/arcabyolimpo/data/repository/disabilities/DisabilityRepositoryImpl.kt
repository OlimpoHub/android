package com.app.arcabyolimpo.data.repository.disabilities

import android.util.Log
import com.app.arcabyolimpo.data.mapper.disabilities.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.repository.disability.DisabilityRepository
import javax.inject.Inject
import kotlin.collections.map

/**
 * Implementation of [DisabilityRepository] that manages data retrieval
 * related to beneficiaries from the remote API.
 *
 * This class uses [ArcaApi] to fetch beneficiary data and maps it into
 * domain models to maintain a clean separation between data and domain layers.
 */

class DisabilityRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
    ) : DisabilityRepository {
    /**
     * Retrieves a list of all available disabilities from the API.
     *
     * Each item in the response is converted into a [Disability] domain model.
     *
     * @return A list of [Disabilities] objects representing the available disabilities.
     */

    override suspend fun getDisabilities(): List<Disability> {
        Log.d("DisabilityRepo", "Llamando a getDisabilities...")

        try {
            val response = api.getDisabilitiesList()
            Log.d("DisabilityRepo", "Éxito: ${response.size} discapacidades recibidas")

            return response.map { dto ->
                Disability(
                    id = dto.id,
                    name = dto.name,
                    characteristics = dto.characteristics
                )
            }
        } catch (e: Exception) {
            Log.e("DisabilityRepo", "Error al obtener discapacidades: ${e.message}")
            Log.e("DisabilityRepo", "Stack trace: ", e)
            throw e
        }
    }

    /**
     * Retrieves the details of the selected disability from the API.
     *
     * The response is converted into a [Disability] domain model.
     *
     * @return A [Disabilities] object representing the available disabilities.
     */
    override suspend fun getDisability(id: String): Disability = api.getDisabilityDetail(id).toDomain()
}