package com.app.arcabyolimpo.data.remote.dto.workshops

import java.sql.Date
import java.sql.Time

data class WorkshopFormData (
    val id: String = "",
    val idTraining: String = "",
    val name: String = "",
    val startHour: Time = Time(0),
    val finishHour: Time = Time(0),
    val status: Int = 1,
    val idUser: String = "",
    val schedule: String = "",
    val date: Date = Date(0),
    val image: String = ""
)