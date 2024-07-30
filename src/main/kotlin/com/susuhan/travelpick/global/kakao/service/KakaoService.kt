package com.susuhan.travelpick.global.kakao.service

import com.susuhan.travelpick.domain.user.repository.UserRepository
import com.susuhan.travelpick.global.auth.dto.request.KakaoLoginRequest
import com.susuhan.travelpick.global.auth.dto.response.TokenResponse
import com.susuhan.travelpick.global.auth.service.AuthCommandService
import com.susuhan.travelpick.global.kakao.client.KakaoApiClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class KakaoService(
    private val kakaoApiClient: KakaoApiClient,
    private val authCommandService: AuthCommandService,
    private val userRepository: UserRepository,
) {

    @Transactional
    fun login(request: KakaoLoginRequest): TokenResponse {
        val userInfo = kakaoApiClient.getUserInfo("Bearer ${request.kakaoAccessToken}")

        // 카카오 로그인이 처음이라면 회원가입을 진행, 아니면 로그인을 진행
        val user = userRepository.findNotDeletedUserBySocialId(userInfo.id)
            ?: userRepository.save(userInfo.toEntity())

        return authCommandService.createJwtTokens(user)
    }
}
