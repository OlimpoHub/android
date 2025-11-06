package com.app.arcabyolimpo.domain.usecase.ExternalCollaborator

import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab
import com.app.arcabyolimpo.domain.repository.ExternalCollabRepository.ExternalCollabRepository
import javax.inject.Inject

class GetCollabByIdUseCase @Inject constructor(
    private val repository: ExternalCollabRepository
) {
    suspend operator fun invoke(id: Int): Result<ExternalCollab> {
        return repository.getCollabById(id)
    }
}