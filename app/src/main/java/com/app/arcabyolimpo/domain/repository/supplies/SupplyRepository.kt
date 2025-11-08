package com.app.arcabyolimpo.domain.repository.supplies

import androidx.navigation.NavType
import com.app.arcabyolimpo.domain.model.supplies.Supply

interface SupplyRepository {
    suspend fun getSuppliesList(): List<Supply>

    suspend fun getSupplyById(id: String): Supply

    suspend fun filterSupply(
        type: String,
        value: String,
    ): List<Supply>

    suspend fun searchSupply(value: String): List<Supply>

    suspend fun orderSupplies(
        type: String,
        value: String,
    ): List<Supply>
}
