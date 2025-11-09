package com.app.arcabyolimpo.domain.model.workshops

import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch

/**
 * Domain model representing a supply item in the system.
 *
 * @property id The unique identifier of the workshop.
 * @property idTraining The unique identifier of the training.
 * @property idUser The unique identifier of the user.
 * @property url The url of the image to be upload.
 * @property nameWorkshop The name of the new workshop.
 * @property startHour The hour for the workshop to be started.
 * @property finishHour The hour for the workshop to be ended.
 * @property status The status of the workshop, active = 1, inactive = 0.
 * @property schedule The time when the workshops is going to happened.
 * @property date The day when the workshop is going to take place.
 */

data class Workshop(
    val id: String,
    val idTraining: String,
    val idUser: String,
    val nameWorkshop: String,
    val url: String,
    val startHour: String,
    val finishHour: String,
    val status: Int,
    val schedule: String,
    val date: String
)