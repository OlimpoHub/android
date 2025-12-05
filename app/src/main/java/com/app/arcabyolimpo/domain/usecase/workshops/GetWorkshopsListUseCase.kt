package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving the full list of workshops.
 *
 * This class encapsulates the logic to load all workshops from the
 * [WorkshopRepository], and exposes the wrapped result in a [Result] flow
 * so the UI can react to loading, success, or error states.
 *
 * [Result.Loading] while the request is in progress.
 * [Result.Success] when the list of workshops is successfully retrieved.
 * [Result.Error] if an exception occurs during the process.
 *
 * @return A [Flow] emitting the state of the operation, containing a list of [Workshop].
 */
class GetWorkshopsListUseCase
@Inject
constructor(
    private val repository: WorkshopRepository
) {
    operator fun invoke(): Flow<Result<List<Workshop>>> =
        flow {
            try {
                emit(Result.Loading)
                val workshops = repository.getWorkshopsList()
                emit(Result.Success(workshops))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
