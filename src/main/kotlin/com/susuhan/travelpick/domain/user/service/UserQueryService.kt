package com.susuhan.travelpick.domain.user.service

import com.susuhan.travelpick.domain.user.dto.request.NicknameCheckRequest
import com.susuhan.travelpick.domain.user.dto.response.NicknameCheckResponse
import com.susuhan.travelpick.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class UserQueryService(
    private val userRepository: UserRepository
) {

    fun checkNicknameDuplicated(nicknameCheckRequest: NicknameCheckRequest): NicknameCheckResponse {
        return NicknameCheckResponse(
            userRepository.existsByNickname(nicknameCheckRequest.nickname)
        )
    }
}