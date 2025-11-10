package com.app.arcabyolimpo.data.remote.dto.workshops

/**
 * DTO representing detailed information of the information when the workshop is added.
 *
 * @param success If the workshop was added successfully.
 * @param message The message shown when the workshop was added successfully.
 * @param idTaller The unique identifier of the workshop.
 */
data class AddNewWorkshopDto(
    val success: Boolean,
    val message: String,
    val idTaller: String? /** new id to be added */
)
