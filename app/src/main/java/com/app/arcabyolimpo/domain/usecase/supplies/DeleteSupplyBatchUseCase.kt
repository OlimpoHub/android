package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/** ---------------------------------------------------------------------------------------------- *
 * DeleteSupplyBatchUseCase -> Use case responsible for deleting a specific supply batch by ID.
 *
 *  * @param repository: SupplyRepository -> repository where the api calls is found
 * ---------------------------------------------------------------------------------------------- */
class DeleteSupplyBatchUseCase
    @Inject
    constructor(
        private val repository: SupplyRepository
    ) {
        operator fun invoke(id: String): Flow<Result<Unit>> =
            flow{
                try{
                    emit(Result.Loading)
                    repository.deleteSupplyBatch(id)
                    emit(Result.Success(Unit))
                }catch (e: Exception){
                    emit(Result.Error(e))
                }
            }
    }