package com.app.arcabyolimpo.domain.repository.supplies

import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch

interface SupplyRepository {
    suspend fun getSuppliesList(): List<Supply>

    suspend fun getSupplyById(id: String): Supply

    suspend fun registerSupplyBatch(batch: SupplyBatch): SupplyBatch
}
