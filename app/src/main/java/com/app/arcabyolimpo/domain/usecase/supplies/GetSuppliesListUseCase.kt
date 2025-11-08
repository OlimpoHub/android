package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSuppliesListUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(): Flow<Result<List<Supply>>> =
            flow {
                try {
                    emit(Result.Loading)
                    val supplies = repository.getSuppliesList()
                    emit(Result.Success(supplies))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
