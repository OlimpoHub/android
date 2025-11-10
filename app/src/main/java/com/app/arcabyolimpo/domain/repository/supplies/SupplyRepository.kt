package com.app.arcabyolimpo.domain.repository.supplies

import com.app.arcabyolimpo.domain.model.supplies.Supply

interface SupplyRepository {
    suspend fun getSuppliesList(): List<Supply>

    suspend fun getSupplyById(id: String): Supply

    suspend fun getSupplyBatchById(id: String): Supply
}
