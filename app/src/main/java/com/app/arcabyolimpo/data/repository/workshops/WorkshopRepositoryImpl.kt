package com.app.arcabyolimpo.data.repository.workshops

import com.app.arcabyolimpo.data.mapper.workshops.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
<<<<<<< HEAD
import java.sql.Date
import java.sql.Time
=======
import java.text.SimpleDateFormat
import java.util.Locale
>>>>>>> e5fc8f949adf43bfa4022a328cb3944f2d60ebba
import javax.inject.Inject

class WorkshopRepositoryImpl
    @Inject constructor(
<<<<<<< HEAD
        private val api: ArcaApi
    ) : WorkshopRepository {

        override suspend fun getWorkshopsList(): List<Workshop> {
            val response = api.getWorkshopsList()
            return response.map { dto ->
                Workshop(
                    id = dto.id,
                    nameWorkshop = dto.name,
                    url = dto.image,
                    status = 0,
                    schedule = "",
                    startHour = Time(0),
                    finishHour = Time(0),
                    date = Date(0)
                )
            }
        }

        override suspend fun getWorkshopsById(id: String): Workshop {
            return api.getWorkshop(id).toDomain()
        }

=======
    private val api: ArcaApi
) : WorkshopRepository {

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override suspend fun getWorkshopsList(): List<Workshop> {
        val response = api.getWorkshopsList()
        return response.map { dto ->
            Workshop(
                id = dto.id,
                url = dto.image,
                nameWorkshop = dto.name.replaceFirstChar { it.uppercase() },
                startHour = timeFormat.format(""),
                finishHour = timeFormat.format(""),
                status = 0,
                schedule = "",
                date = dateFormat.format("")
            )
        }
    }

    override suspend fun getWorkshopById(id: String): Workshop{
        return api.getWorkshop(id).toDomain()
    }
>>>>>>> e5fc8f949adf43bfa4022a328cb3944f2d60ebba
}