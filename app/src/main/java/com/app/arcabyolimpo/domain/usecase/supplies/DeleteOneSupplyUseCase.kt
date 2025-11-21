package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for deleting a single supply.
 *
 * This class encapsulates the logic to trigger the delete operation
 * through the [SupplyRepository], and exposes the wrapped result
 * in a [Result] flow so the UI can react to the
 * load, success, or error states.
 * [Result.Loading] while the operation is running.
 * [Result.Success] when the deletion is completed successfully.
 * [Result.Error] if an exception occurs during the process.
 *
 */
class DeleteOneSupplyUseCase
@Inject
constructor(
    // Repository that provides access to supply-related operations.
    private val repository: SupplyRepository
){
    //Pull the ID and invoke the flow
    operator fun invoke(id: String): Flow<Result<Unit>> =
        flow{
            try{
                emit(Result.Loading)
                repository.deleteOneSupply(id)
                emit(Result.Success(Unit))
            }catch (e: Exception){
                emit(Result.Error(e))
            }
        }
}