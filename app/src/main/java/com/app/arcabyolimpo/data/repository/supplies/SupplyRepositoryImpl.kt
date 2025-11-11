package com.app.arcabyolimpo.data.repository.supplies

import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import javax.inject.Inject

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

        override suspend fun getSupplyBatchById(id: String): SupplyBatchExt = api.getSupplyBatch(id).toDomain()
    }
