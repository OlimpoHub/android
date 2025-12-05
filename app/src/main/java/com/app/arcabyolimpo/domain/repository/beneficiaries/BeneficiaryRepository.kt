package com.app.arcabyolimpo.domain.repository.beneficiaries

import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.domain.model.filter.FilterData

interface BeneficiaryRepository {
    suspend fun getBeneficiariesList(): List<Beneficiary>

    suspend fun getBeneficiaryById(id: String): Beneficiary

    suspend fun deleteBeneficiary(id: String)

    /**
     * Retrieves all available disability-related filter data for beneficiaries.
     *
     * This function fetches the disability categories or other metadata required
     * to build the filter UI for beneficiary management. The raw API response
     * is expected to be mapped into the domain model [FilterData] by the
     * implementation of this interface.
     *
     * @return [FilterData] containing disability options and related filter data.
     */
    suspend fun getDisabilitiesData(): FilterData

    /**
     * Filters beneficiaries based on the provided criteria.
     *
     * This function receives a [FilterDto] containing filtering parameters
     * (such as name, disability, workshop, status, or other attributes)
     * and returns a list of domain model [Beneficiary] objects that match
     * the given filter configuration.
     *
     * @param params Filter criteria encapsulated in a [FilterDto].
     * @return A list of filtered [Beneficiary] objects.
     */
    suspend fun filterBeneficiary(params: FilterDto): List<Beneficiary>

    suspend fun searchBeneficiaries(query: String): List<Beneficiary>

    suspend fun addBeneficiary(newBeneficiary: BeneficiaryDto): Beneficiary

    suspend fun modifyBeneficiary(modifiedBeneficiary: BeneficiaryDto): Beneficiary
}
