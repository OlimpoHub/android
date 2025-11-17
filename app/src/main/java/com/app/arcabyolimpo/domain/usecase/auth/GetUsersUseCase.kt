package com.app.arcabyolimpo.domain.usecase.user

import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(): Result<List<UserDto>> {
        return usersRepository.getUsersDomain()
    }
}