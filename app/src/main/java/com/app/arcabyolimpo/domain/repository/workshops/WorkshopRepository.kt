package com.app.arcabyolimpo.domain.repository.workshops

import com.app.arcabyolimpo.domain.model.workshops.Workshop

interface WorkshopRepository {

    suspend fun getWorkshopsList(): List<Workshop>

<<<<<<< HEAD
    suspend fun getWorkshopsById(id: String): Workshop
=======
    suspend fun getWorkshopById(id: String): Workshop
>>>>>>> e5fc8f949adf43bfa4022a328cb3944f2d60ebba
}