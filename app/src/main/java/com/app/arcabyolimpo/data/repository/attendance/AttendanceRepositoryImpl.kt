package com.app.arcabyolimpo.data.repository.attendance

import com.app.arcabyolimpo.data.mapper.attendance.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.attendance.AttendanceDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.attendance.AttendanceRepository
import javax.inject.Inject

class AttendanceRepositoryImpl (
    private val api: ArcaApi,
) : AttendanceRepository {

    override suspend fun getAttendanceByUser(userId: String): List<AttendanceDto> {
        return api.getAttendanceByUser(userId)
    }

}