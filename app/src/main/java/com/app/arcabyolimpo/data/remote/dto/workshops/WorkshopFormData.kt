package com.app.arcabyolimpo.data.remote.dto.workshops

/**
 * DTO representing detailed information the workshop data before being overwritten.
 *
 * @param id The unique identifier of the workshop.
 * @param idTraining The unique identifier of the training.
 * @param idUser The unique identifier of the user.
 * @param image The url of the image to be upload.
 * @param name The name of the new workshop.
 * @param startHour The hour for the workshop to be started.
 * @param finishHour The hour for the workshop to be ended.
 * @param status The status of the workshop, active = 1, inactive = 0.
 * @param schedule The time when the workshops is going to happened.
 * @param date The day when the workshop is going to take place.
 */
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