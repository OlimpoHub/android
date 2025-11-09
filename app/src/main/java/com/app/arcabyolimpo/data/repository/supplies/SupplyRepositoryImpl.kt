package com.app.arcabyolimpo.data.repository.supplies

import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.mapper.supplies.toFilterData
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.supplies.FilterSuppliesDto
import com.app.arcabyolimpo.domain.model.supplies.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import javax.inject.Inject

/**
 * Extension function to convert a [SupplyDto] from the data (remote) layer
 * into a [Supply] domain model. This mapping ensures the domain layer remains
 * independent from data transfer object (DTO) structures.
 *
 * It capitalizes the first character of the supply name and maps each batch
 * from the DTO into a [SupplyBatch] domain model.
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
            return response.map { it.toDomain() }
        }

        override suspend fun getFilterData(): FilterData = api.getFilterSupplies().toFilterData()
    }
