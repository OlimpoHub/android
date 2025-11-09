package com.app.arcabyolimpo.data.remote.dto.workshops

data class WorkshopFormData (
    val id: String = "",
    val idTraining: String = "",
    val name: String = "",
    val startHour: String = "",
    val finishHour: String = "",
    val status: Int = 1,
    val idUser: String = "",
    val schedule: String = "",
    val date: String = "",
    val image: String = ""
)