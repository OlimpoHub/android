package com.app.arcabyolimpo.domain.usecase.ExternalCollaborator

import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab
import com.app.arcabyolimpo.domain.repository.ExternalCollabRepository.ExternalCollabRepository
import javax.inject.Inject

class GetAllCollabsUseCase @Inject constructor(
    private val repository: ExternalCollabRepository
) {
    suspend operator fun invoke(): Result<List<ExternalCollab>> {
        return repository.getAllCollabs()
    }
}