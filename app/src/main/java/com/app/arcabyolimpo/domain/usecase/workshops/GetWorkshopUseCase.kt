package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for retrieving a single workshop by its ID.
 *
 * This class requests a workshop from the [WorkshopRepository] and exposes
 * the wrapped response inside a [Result] flow, enabling the UI to react
 * according to the state of the operation.
 *
 * [Result.Loading] while the request is being processed.
 * [Result.Success] when the workshop is found and returned.
 * [Result.Error] if the workshop does not exist or an exception occurs.
 *
 * @param id Unique identifier of the workshop.
 * @return A [Flow] emitting the operation state with the requested [Workshop].
 */
class GetWorkshopUseCase
@Inject
constructor(
    private val repository: WorkshopRepository
) {
    operator fun invoke(id: String): Flow<Result<Workshop>> =
        flow {
            try {
                emit(Result.Loading)
                val workshop = repository.getWorkshopsById(id)

                if (workshop != null) {
                    emit(Result.Success(workshop))
                } else {
                    emit(Result.Error(Exception("Taller no encontrado con ID: $id")))
                }

            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
