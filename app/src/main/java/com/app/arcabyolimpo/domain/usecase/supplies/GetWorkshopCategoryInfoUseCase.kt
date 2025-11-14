package com.app.arcabyolimpo.domain.usecase.supplies

import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import javax.inject.Inject
/** ---------------------------------------------------------------------------------------------- *
 * GetWorkshopCategoryInfoUseCase -> Use case that collects all the workshops and categories from the
 * db id and name
 *
 * @param repository: SupplyRepository -> repository where the api calls is found
 * ---------------------------------------------------------------------------------------------- */
class GetWorkshopCategoryInfoUseCase @Inject constructor(
    private val repository: SupplyRepository
) {
    suspend operator fun invoke() = repository.getWorkshopCategoryList()
}