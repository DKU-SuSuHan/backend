package com.susuhan.travelpick.global.auth.service

import com.susuhan.travelpick.domain.user.dto.UserDto
import com.susuhan.travelpick.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class AuthQueryService(
    private val userRepository: UserRepository
) {

    fun findBySocialId(socialId: String): UserDto? {
        return userRepository.findBySocialId(socialId)?.let { user -> UserDto.from(user) }
    }
}