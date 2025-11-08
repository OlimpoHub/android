package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSupplyUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(id: String): Flow<Result<Supply>> =
            flow {
                try {
                    emit(Result.Loading)
                    val supply = repository.getSupplyById(id)
                    emit(Result.Success(supply))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
