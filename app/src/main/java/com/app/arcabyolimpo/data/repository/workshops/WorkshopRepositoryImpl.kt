package com.app.arcabyolimpo.data.repository.workshops

import com.app.arcabyolimpo.data.mapper.workshops.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class WorkshopRepositoryImpl
    @Inject constructor(
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
}