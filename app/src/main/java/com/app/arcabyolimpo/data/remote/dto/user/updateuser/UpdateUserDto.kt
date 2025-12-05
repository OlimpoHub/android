package com.app.arcabyolimpo.data.remote.dto.user.updateuser

import com.google.gson.annotations.SerializedName

/**
 * Data transfer object for user update API requests.
 *
 * This DTO represents the structure required by the backend update endpoint,
 * mapping domain user data to the specific field names and format expected by the API.
 * Unlike the registration DTO, this includes the user's ID to identify which record
 * to update. All other properties follow the "externalCollab_" prefix convention
 * required by the backend, with Gson annotations handling the serialization between
 * Kotlin property names and the API's field naming scheme.
 *
 * The DTO includes both required fields for basic user information and optional fields
 * for compliance documents and additional user data. Default values are provided for
 * optional compliance-related fields to support partial updates without affecting
 * previously set values.
 *
 * @property id The unique identifier of the user to be updated. Required to specify
 *              which user record should be modified in the backend.
 * @property roleId The unique identifier of the role assigned to the user. Determines
 *                  the user's permissions and access level within the system.
 * @property name The user's first name. Optional to support flexible update flows where
 *                only specific fields need modification.
 * @property lastName The user's paternal last name (apellido paterno). Optional to support
 *                    flexible update flows.
 * @property secondLastName The user's maternal last name (apellido materno). Required by
 *                          the backend for complete user identification.
 * @property birthDate The user's date of birth in YYYY-MM-DD format. Used for age verification
 *                     and demographic purposes.
 * @property degree The user's academic degree or career field. Provides context about the
 *                  user's professional or educational background.
 * @property email The user's email address. Optional to support various update scenarios,
 *                 though typically required for account verification and communication.
 * @property phone The user's phone number. Optional contact information for notifications
 *                 and account recovery purposes.
 * @property status The user's account status represented as an integer. Controls whether
 *                  the account is active, pending, suspended, or in another state.
 * @property internalRegulation Acceptance status of internal regulations (0 or 1). Null indicates
 *                              no change to the existing value. Defaults to null to preserve
 *                              the current compliance status if not being updated.
 * @property idCopy Status indicating whether an ID copy has been provided (0 or 1). Null indicates
 *                  no change to the existing value. Defaults to null to preserve the current
 *                  document status if not being updated.
 * @property confidentialityNotice Acceptance status of the confidentiality notice (0 or 1). Null indicates
 *                                 no change to the existing value. Defaults to null to preserve the
 *                                 current compliance status if not being updated.
 * @property foto The user's profile photo as a Base64-encoded string or URL. Optional field
 *                for visual identification and profile personalization. Null preserves the
 *                existing photo.
 */
data class UpdateUserDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("externalCollab_roleId")
    val roleId: String,

    @SerializedName("externalCollab_name")
    val name: String?,

    @SerializedName("externalCollab_lastName")
    val lastName: String?,

    @SerializedName("externalCollab_secondLastName")
    val secondLastName: String,

    @SerializedName("externalCollab_birthDate")
    val birthDate: String,

    @SerializedName("externalCollab_degree")
    val degree: String,

    @SerializedName("externalCollab_email")
    val email: String?,

    @SerializedName("externalCollab_phone")
    val phone: String?,

    @SerializedName("externalCollab_status")
    val status: Int,

    @SerializedName("externalCollab_internalRegulation")
    val internalRegulation: Int? = null,

    @SerializedName("externalCollab_idCopy")
    val idCopy: Int? = null,

    @SerializedName("externalCollab_confidentialityNotice")
    val confidentialityNotice: Int? = null,

    @SerializedName("externalCollab_photo")
    val foto: String?
)