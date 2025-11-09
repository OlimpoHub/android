package com.app.arcabyolimpo.data.repository.workshops

import com.app.arcabyolimpo.data.mapper.workshops.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository

import javax.inject.Inject

class WorkshopRepositoryImpl
    @Inject constructor(
        private val api: ArcaApi
    ) : WorkshopRepository {

        override suspend fun getWorkshopsList(): List<Workshop> {
            val response = api.getWorkshopsList()
            return response.map { dto ->
                Workshop(
                    id = dto.id,
                    idTraining = dto.idTraining,
                    idUser = dto.idUser,
                    nameWorkshop = dto.name,
                    url = dto.image,
                    status = 0,
                    schedule = "",
                    startHour = "",
                    finishHour = "",
                    date = ""
                )
            }
        }

        override suspend fun getWorkshopsById(id: String): Workshop {
            return api.getWorkshop(id).toDomain()
        }

        override suspend fun addWorkshop(newWorkshop: WorkshopDto): Workshop {
            val response = api.addWorkshop(newWorkshop)
            return Workshop(
                id = newWorkshop.id,
                idTraining = newWorkshop.idTraining,
                idUser = newWorkshop.idUser,
                nameWorkshop = newWorkshop.name,
                url = newWorkshop.image,
                status = newWorkshop.status,
                schedule = newWorkshop.schedule,
                startHour = newWorkshop.startHour,
                finishHour = newWorkshop.finishHour,
                date = newWorkshop.date
            )
        }

}