package com.app.arcabyolimpo.data.repository.supplies

import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.supplies.FilterSuppliesDto
import com.app.arcabyolimpo.domain.model.supplies.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import jakarta.inject.Singleton
import javax.inject.Inject

/**
 * Retrieves detailed information for a specific supply by its [id].
 *
 * This function calls the API to obtain the corresponding [SupplyDto]
 * and converts it into a [Supply] domain model using the [toDomain] mapper.
 *
 * @param id The unique identifier of the supply to retrieve.
 * @return A [Supply] object containing detailed supply information.
 */

class SupplyRepositoryImpl
    @Inject
    constructor(
        private val api: ArcaApi,
    ) : SupplyRepository {
        override suspend fun getSuppliesList(): List<Supply> {
            val response = api.getSuppliesList()
            return response.map { dto ->
                Supply(
                    id = dto.id,
                    name = dto.name,
                    imageUrl = dto.image,
                    unitMeasure = "",
                    batch = emptyList(),
                )
            }
        }

        override suspend fun getSupplyById(id: String): Supply = api.getSupply(id).toDomain()

        override suspend fun filterSupply(filters: FilterSuppliesDto): List<Supply> {
            val response = api.filterSupplies(filters)
            println("DEBUG FILTER RESPONSE: $response")
            return response.map { dto ->
                Supply(
                    id = dto.id,
                    name = dto.name,
                    imageUrl = dto.image,
                    unitMeasure = "",
                    batch = emptyList(),
                )
            }
        }

        override suspend fun getFilterData(): FilterData = api.getFilterSupplies().toDomain()
    }
