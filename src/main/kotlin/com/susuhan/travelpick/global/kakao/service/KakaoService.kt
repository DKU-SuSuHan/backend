package com.susuhan.travelpick.global.kakao.service

import com.susuhan.travelpick.global.auth.dto.request.KakaoLoginRequest
import com.susuhan.travelpick.global.auth.dto.response.TokenResponse
import com.susuhan.travelpick.global.auth.service.AuthCommandService
import com.susuhan.travelpick.global.auth.service.AuthQueryService
import com.susuhan.travelpick.global.kakao.client.KakaoApiClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class KakaoService(
    private val kakaoApiClient: KakaoApiClient,
    private val authQueryService: AuthQueryService,
    private val authCommandService: AuthCommandService
) {

    @Transactional
    fun login(requst: KakaoLoginRequest): TokenResponse {
        // 카카오 로그인이 처음이라면 회원가입을 진행, 아니면 로그인을 진행
        val userInfo = kakaoApiClient.getUserInfo("Bearer ${requst.kakaoAccessToken}")
        val userDto = authQueryService.findBySocialId(userInfo.id) ?: authCommandService.join(userInfo.toUserDto())

        return authCommandService.createJwtTokens(userDto.id!!, userDto.role!!)
    }
}