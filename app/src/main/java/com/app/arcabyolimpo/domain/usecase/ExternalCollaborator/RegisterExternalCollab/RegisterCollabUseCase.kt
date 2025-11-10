package com.app.arcabyolimpo.domain.usecase.ExternalCollaborator.RegisterExternalCollab

import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab
import com.app.arcabyolimpo.domain.repository.ExternalCollabRepository.ExternalCollabRepository
import javax.inject.Inject

class RegisterCollabUseCase @Inject constructor(
    private val repository: ExternalCollabRepository
) {
    suspend operator fun invoke(collab: ExternalCollab): Result<ExternalCollab> {
        return repository.registerCollab(collab)
    }
}
