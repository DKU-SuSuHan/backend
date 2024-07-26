package com.susuhan.travelpick.domain.user.service

import com.susuhan.travelpick.domain.user.dto.request.NicknameUpdateRequest
import com.susuhan.travelpick.domain.user.dto.response.NicknameUpdateResponse
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserCommandService(
    private val userRepository: UserRepository,
) {

    @Transactional
    fun updateNickname(userId: Long, nicknameUpdateRequest: NicknameUpdateRequest): NicknameUpdateResponse {
        val user = userRepository.findNotDeletedUser(userId)
            ?: throw UserIdNotFoundException()

        user.updateNickname(nicknameUpdateRequest.nickname)

        return NicknameUpdateResponse.from(user)
    }
}
