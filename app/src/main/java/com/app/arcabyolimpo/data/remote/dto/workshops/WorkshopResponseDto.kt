package com.app.arcabyolimpo.data.remote.dto.workshops
import com.google.gson.annotations.SerializedName

data class WorkshopResponseDto(
    @SerializedName("workshop") val workshopList: List<WorkshopDto>,
    @SerializedName("beneficiaries") val beneficiaries: List<Any>
)
