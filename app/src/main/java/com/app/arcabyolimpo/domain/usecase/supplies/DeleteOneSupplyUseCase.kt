package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeleteOneSupplyUseCase
@Inject
constructor(
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