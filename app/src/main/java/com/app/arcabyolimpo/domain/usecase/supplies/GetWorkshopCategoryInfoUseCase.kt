package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import javax.inject.Inject

class GetWorkshopCategoryInfoUseCase @Inject constructor(
    private val repository: SupplyRepository
) {
    suspend operator fun invoke() = repository.getWorkshopCategoryList()
}