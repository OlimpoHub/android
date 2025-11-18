package com.app.arcabyolimpo.domain.usecase.disabilities

import com.app.arcabyolimpo.domain.repository.disability.DisabilityRepository
import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.common.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDisabilitiesUseCase
    @Inject
    constructor(
        private val repository: DisabilityRepository

    ) {
    operator fun invoke(): Flow<Result<List<Disability>>> =
        flow {
            try {
                emit(Result.Loading)
                val disabilities = repository.getDisabilities()
                emit(Result.Success(disabilities))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
