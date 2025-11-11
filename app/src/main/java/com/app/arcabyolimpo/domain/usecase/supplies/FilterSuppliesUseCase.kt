package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.data.remote.dto.supplies.FilterDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FilterSuppliesUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        suspend fun filterSupplies(params: FilterDto): List<Supply> = repository.filterSupply(params)

        operator fun invoke(params: FilterDto): Flow<com.app.arcabyolimpo.domain.common.Result<List<Supply>>> =
            flow {
                try {
                    emit(com.app.arcabyolimpo.domain.common.Result.Loading)
                    val supplies = repository.filterSupply(params)
                    emit(
                        com.app.arcabyolimpo.domain.common.Result
                            .Success(supplies),
                    )
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
