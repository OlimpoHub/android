package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFiltersDataUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(): Flow<Result<FilterData>> =
            flow {
                try {
                    emit(Result.Loading)
                    val filter = repository.getFilterData()
                    emit(Result.Success(filter))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
