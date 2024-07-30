package com.susuhan.travelpick.global.auth.service

import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.global.auth.dto.response.TokenResponse
import com.susuhan.travelpick.global.security.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthCommandService(
    private val jwtTokenProvider: JwtTokenProvider,
) {

    @Transactional
    fun createJwtTokens(user: User): TokenResponse {
        val accessToken = jwtTokenProvider.createAccessToken(user.id!!, user.role)
        val refreshToken = jwtTokenProvider.createRefreshToken(user.id!!, user.role)

        user.updateRefreshToken(refreshToken)

        return TokenResponse.of(accessToken, refreshToken)
    }
}
