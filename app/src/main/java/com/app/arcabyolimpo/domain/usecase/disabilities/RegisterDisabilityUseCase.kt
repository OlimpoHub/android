package com.app.arcabyolimpo.domain.usecase.disabilities

import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.repository.disability.DisabilityRepository
import javax.inject.Inject

/**
 * A use case for registering a new disability. *
 * This class encapsulates the business logic for creating a new disability entry,
 * acting as a bridge between the ViewModel and the repository.
 *
 * @param repository The repository responsible for handling disability data operations.
 */
class RegisterDisabilityUseCase
    @Inject
    constructor(
        private val repository: DisabilityRepository,
    ) {
        suspend operator fun invoke(disability: Disability) = repository.registerDisability(disability)
    }
