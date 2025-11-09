package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/** Use case that encapsulates the business logic for retrieving a specific supply by its ID. */
class GetSupplyUseCase
@Inject
constructor(
    private val repository: SupplyRepository
) {
    /**
     * Executes the flow to retrieve a supply by its [id].
     *
     * Emits:
     * - [Result.Loading] while the supply data is being fetched.
     * - [Result.Success] with the corresponding [Supply] on success.
     * - [Result.Error] if an exception occurs during retrieval.
     *
     * @param id The unique identifier of the supply to fetch.
     * @return A [Flow] representing the state of the supply retrieval process.
     */
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
