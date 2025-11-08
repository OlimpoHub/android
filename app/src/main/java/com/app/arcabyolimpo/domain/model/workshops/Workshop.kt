package com.app.arcabyolimpo.domain.model.workshops

data class Workshop(
    val id: String,
    val nameWorkshop: String,
    val url: String,
    val startHour: String,
    val finishHour: String,
    val status: Int,
    val schedule: String,
    val date: String
)