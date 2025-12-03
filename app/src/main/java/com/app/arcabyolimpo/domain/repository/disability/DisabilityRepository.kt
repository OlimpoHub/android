package com.app.arcabyolimpo.domain.repository.disability

import com.app.arcabyolimpo.domain.model.disabilities.Disability

interface DisabilityRepository {
    suspend fun getDisabilities(): List<Disability>

    suspend fun registerDisability(disability: Disability)
    
    suspend fun getDisability(id: String): Disability

    suspend fun deleteDisability(id: String)
}
