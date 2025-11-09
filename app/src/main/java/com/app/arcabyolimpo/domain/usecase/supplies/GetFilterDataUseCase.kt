@file:Suppress("ktlint:standard:filename")

package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.FilterData
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFilterDataUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(): Flow<Result<FilterData>> =
            flow {
                try {
                    emit(Result.Loading)
                    val data = repository.getFilterData()
                    emit(Result.Success(data))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
