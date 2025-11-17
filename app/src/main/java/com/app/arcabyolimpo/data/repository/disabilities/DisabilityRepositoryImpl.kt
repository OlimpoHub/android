package com.app.arcabyolimpo.data.repository.disabilities

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
        val response = api.getDisabilities()
        return response.map { dto ->
            Disability (
                id = dto.id.orEmpty(),
                name = dto.name.orEmpty(),
                characteristics =  dto.characteristics.orEmpty(),
            )
        }
        }
    }