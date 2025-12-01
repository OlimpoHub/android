package com.app.arcabyolimpo.domain.usecase.attendance

import com.app.arcabyolimpo.data.remote.dto.attendance.AttendanceDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.attendance.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAttendanceByUserUseCase @Inject constructor(
    private val repository: AttendanceRepository,
) {
    operator fun invoke(userId: String): Flow<Result<List<AttendanceDto>>> =
        flow {
            try {
                emit(Result.Loading)
                val attendances = repository.getAttendanceByUser(userId)
                emit(Result.Success(attendances))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}