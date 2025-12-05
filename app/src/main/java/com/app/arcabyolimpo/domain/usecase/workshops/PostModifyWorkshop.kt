package com.app.arcabyolimpo.domain.usecase.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Use case responsible for modifying an existing workshop.
 *
 * This class receives a modified [WorkshopDto], sends the update request to
 * the [WorkshopRepository], and exposes the wrapped result in a [Result] flow.
 * The UI can observe state changes to handle loading, success, or error events.
 *
 * [Result.Loading] while the workshop update is being processed.
 * [Result.Success] when the workshop is successfully updated.
 * [Result.Error] if an exception occurs during the operation.
 *
 * @param modifiedWorkshop Data of the workshop to be updated.
 * @return A [Flow] emitting the update operation state.
 */
class PostModifyWorkshop
@Inject
constructor(
    private val repository: WorkshopRepository
) {
    operator fun invoke(modifiedWorkshop: WorkshopDto): Flow<Result<Workshop>> =
        flow {
            try {
                emit(Result.Loading)
                val workshop = repository.modifyWorkshop(modifiedWorkshop)
                emit(Result.Success(workshop))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}
