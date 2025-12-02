package com.app.arcabyolimpo.domain.repository.attendance

import com.app.arcabyolimpo.data.remote.dto.attendance.AttendanceDto
import com.app.arcabyolimpo.domain.common.Result

interface AttendanceRepository {

    suspend fun getAttendanceByUser(userId: String): List<AttendanceDto>
}
