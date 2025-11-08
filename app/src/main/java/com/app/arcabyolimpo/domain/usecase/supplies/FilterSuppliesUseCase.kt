package com.app.arcabyolimpo.domain.usecase.supplies

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
        suspend fun filterSupplies(
            type: String,
            value: String,
        ): List<Supply> = repository.filterSupply(type, value)

        operator fun invoke(
            type: String,
            value: String,
        ): Flow<com.app.arcabyolimpo.domain.common.Result<List<Supply>>> =
            flow {
                try {
                    emit(com.app.arcabyolimpo.domain.common.Result.Loading)
                    val supplies = repository.filterSupply(type, value)
                    emit(
                        com.app.arcabyolimpo.domain.common.Result
                            .Success(supplies),
                    )
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
