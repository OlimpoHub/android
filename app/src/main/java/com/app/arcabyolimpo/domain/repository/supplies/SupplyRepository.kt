package com.app.arcabyolimpo.domain.repository.supplies

import androidx.navigation.NavType
import com.app.arcabyolimpo.data.remote.dto.supplies.FilterSuppliesDto
import com.app.arcabyolimpo.domain.model.supplies.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import retrofit2.http.Body

interface SupplyRepository {
    suspend fun getSuppliesList(): List<Supply>

    suspend fun getSupplyById(id: String): Supply

    suspend fun filterSupply(filters: FilterSuppliesDto): List<Supply>

    suspend fun getFilterData(): FilterData
}
