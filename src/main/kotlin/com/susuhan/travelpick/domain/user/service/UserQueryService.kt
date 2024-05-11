package com.susuhan.travelpick.domain.user.service

import com.susuhan.travelpick.domain.user.dto.response.NicknameCheckResponse
import com.susuhan.travelpick.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserQueryService(
    private val userRepository: UserRepository
) {

    fun checkNicknameDuplicated(nickname: String): NicknameCheckResponse {
        val isDuplicated = userRepository.existsByNickname(nickname)
        return NicknameCheckResponse.of(isDuplicated)
    }
}