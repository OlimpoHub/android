package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for adding a new workshop.
 *
 * This class sends the provided [WorkshopDto] to the [WorkshopRepository]
 * and exposes the result wrapped in a [Result] flow, allowing the UI to
 * observe loading, success, and error events.
 *
 * [Result.Loading] while the workshop is being created.
 * [Result.Success] when the workshop is successfully added.
 * [Result.Error] if an exception occurs during creation.
 *
 * @param newWorkshop Data of the new workshop to be added.
 * @return A [Flow] emitting the state of the creation operation.
 */
class PostAddNewWorkshop
@Inject
constructor(
    private val repository: WorkshopRepository
) {
    operator fun invoke(newWorkshop: WorkshopDto): Flow<Result<Workshop>> =
        flow {
            try {
                emit(Result.Loading)
                val workshop = repository.addWorkshop(newWorkshop)
                emit(Result.Success(workshop))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
