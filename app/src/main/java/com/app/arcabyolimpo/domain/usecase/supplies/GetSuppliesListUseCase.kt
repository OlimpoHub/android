package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/** Use case that encapsulates the business logic for retrieving the list of supplies. */
class GetSuppliesListUseCase
@Inject
constructor(
    private val repository: SupplyRepository
) {
    /**
     * Executes the flow to retrieve all available supplies.
     *
     * Emits:
     * - [Result.Loading] while data is being fetched.
     * - [Result.Success] with a list of [Supply] on success.
     * - [Result.Error] if an exception occurs during the process.
     *
     * @return A [Flow] representing the state of the supply list retrieval process.
     */
    operator fun invoke(): Flow<Result<List<Supply>>> =
        flow {
            try {
                emit(Result.Loading)
                val supplies = repository.getSuppliesList()
                emit(Result.Success(supplies))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
