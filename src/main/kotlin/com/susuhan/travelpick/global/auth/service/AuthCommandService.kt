package com.susuhan.travelpick.global.auth.service

import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.domain.user.service.UserQueryService
import com.susuhan.travelpick.global.auth.dto.request.RenewalTokensRequest
import com.susuhan.travelpick.global.auth.dto.response.TokenResponse
import com.susuhan.travelpick.global.auth.repository.RefreshTokenRedisRepository
import com.susuhan.travelpick.global.security.JwtTokenProvider
import com.susuhan.travelpick.global.security.exception.TokenNotValidException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthCommandService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserQueryService,
    private val refreshTokenRedisRepository: RefreshTokenRedisRepository,
) {

    @Transactional
    fun createJwtTokens(user: User): TokenResponse {
        val accessToken = jwtTokenProvider.createAccessToken(user.id!!, user.role)
        val refreshToken = jwtTokenProvider.createRefreshToken(user.id!!, user.role)

        refreshTokenRedisRepository.save(user.id!!, refreshToken)

        return TokenResponse.of(accessToken, refreshToken)
    }

    @Transactional
    fun renewalJwtTokens(request: RenewalTokensRequest): TokenResponse {
        val refreshToken = request.refreshToken
        val userId = jwtTokenProvider.getUserId(refreshToken)

        validateRefreshToken(userId, refreshToken)

        return createJwtTokens(
            userService.getUserById(userId.toLong()),
        )
    }

    private fun validateRefreshToken(userId: String, refreshToken: String) {
        val validRefreshToken = refreshTokenRedisRepository.findRefreshToken(userId)

        if (validRefreshToken == null || validRefreshToken != refreshToken) {
            throw TokenNotValidException()
        }
    }
}
