package com.app.arcabyolimpo.domain.usecase.ExternalCollaborator

import com.app.arcabyolimpo.domain.repository.ExternalCollabRepository.ExternalCollabRepository
import javax.inject.Inject

class DeleteExternalCollabUseCase
    @Inject
    constructor(
        private val repository: ExternalCollabRepository,
    ) {
        suspend operator fun invoke(id: String): Result<Boolean> = repository.deleteCollab(id)
    }
