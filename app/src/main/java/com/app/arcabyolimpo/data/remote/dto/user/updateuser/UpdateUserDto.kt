package com.app.arcabyolimpo.data.remote.dto.user.updateuser

import com.google.gson.annotations.SerializedName

data class UpdateUserDto(
    @SerializedName("id")
    val id: String,

    @SerializedName("externalCollab_roleId")
    val roleId: String,

    @SerializedName("externalCollab_name")
    val name: String,

    @SerializedName("externalCollab_lastName")
    val lastName: String,

    @SerializedName("externalCollab_secondLastName")
    val secondLastName: String,

    @SerializedName("externalCollab_birthDate")
    val birthDate: String,

    @SerializedName("externalCollab_degree")
    val degree: String,

    @SerializedName("externalCollab_email")
    val email: String,

    @SerializedName("externalCollab_phone")
    val phone: String,

    @SerializedName("externalCollab_status")
    val status: Int,

    @SerializedName("externalCollab_internalRegulation")
    val internalRegulation: Int? = null,

    @SerializedName("externalCollab_idCopy")
    val idCopy: Int? = null,

    @SerializedName("externalCollab_confidentialityNotice")
    val confidentialityNotice: Int? = null
)