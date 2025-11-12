package com.app.arcabyolimpo.data.remote.dto.workshops

/**
 * DTO representing detailed information of the information when the workshop is modified.
 *
 * @param success If the workshop was added successfully.
 * @param message The message shown when the workshop was added successfully.
 *
*/

data class ModifyWorkshopsDto (
    val success: Boolean,
    val message: String
)