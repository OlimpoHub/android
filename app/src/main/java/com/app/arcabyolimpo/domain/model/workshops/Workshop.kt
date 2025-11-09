package com.app.arcabyolimpo.domain.model.workshops

import java.sql.Date
import java.sql.Time

data class Workshop(
    val id: String,
    val idTraining: String,
    val idUser: String,
    val nameWorkshop: String,
    val url: String,
    val startHour: Time,
    val finishHour: Time,
    val status: Int,
    val schedule: String,
    val date: Date
)