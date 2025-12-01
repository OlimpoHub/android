package com.app.arcabyolimpo.domain.usecase.disabilities

import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.repository.disability.DisabilityRepository
import javax.inject.Inject

class RegisterDisabilityUseCase
    @Inject
    constructor(
        private val repository: DisabilityRepository,
    ) {
        suspend operator fun invoke(disability: Disability) = repository.registerDisability(disability)
    }
