package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Acquisition
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving a list of acquisition types.
 *
 * @property repository The repository that communicates with the remote API.
 * @constructor Creates an instance of [GetAcquisitionTypesUseCase].
 * @throws Exception If an error occurs during the process.
 * @return A flow emitting the result of the operation.
 * @see Result
 */
class GetAcquisitionTypesUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository,
    ) {
        operator fun invoke(): Flow<Result<List<Acquisition>>> =
            flow {
                try {
                    emit(Result.Loading)
                    val supplies = repository.getAcquisitionTypes()
                    emit(Result.Success(supplies))
                } catch (e: Exception) {
                    emit(Result.Error(e))
                }
            }
    }
