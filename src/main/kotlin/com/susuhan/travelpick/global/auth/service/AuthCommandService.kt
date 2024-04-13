package com.susuhan.travelpick.global.auth.service

import com.susuhan.travelpick.domain.user.constant.Role
import com.susuhan.travelpick.domain.user.dto.UserDto
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import com.susuhan.travelpick.global.auth.dto.response.TokenResponse
import com.susuhan.travelpick.global.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class AuthCommandService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userRepository: UserRepository
) {

    @Transactional
    fun join(userDto: UserDto): UserDto {
        val user = userRepository.save(userDto.toEntity())
        return UserDto.from(user)
    }

    @Transactional
    fun createJwtTokens(userId: Long, role: Role): TokenResponse {
        val user = userRepository.findById(userId) ?: throw UserIdNotFoundException()
        val accessToken = jwtTokenProvider.createAccessToken(userId, role)
        val refreshToken = jwtTokenProvider.createRefreshToken(userId, role)

        user.updateRefreshToken(refreshToken)

        return TokenResponse(accessToken, refreshToken)
    }
}