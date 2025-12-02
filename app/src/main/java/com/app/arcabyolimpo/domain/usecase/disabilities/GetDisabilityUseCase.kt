package com.app.arcabyolimpo.domain.usecase.disabilities

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.repository.disability.DisabilityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetDisabilityUseCase
@Inject
constructor(
    private val repository: DisabilityRepository
) {
    operator fun invoke(id: String): Flow<Result<Disability>> =
        flow {
            try {
                emit(Result.Loading)
                val disability = repository.getDisability(id)
                emit(Result.Success(disability))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}