package com.susuhan.travelpick.domain.auth.service

import com.susuhan.travelpick.domain.auth.dto.KakaoUserInfo
import com.susuhan.travelpick.domain.auth.dto.request.KakaoLoginRequest
import com.susuhan.travelpick.domain.auth.dto.response.TokenResponse
import com.susuhan.travelpick.domain.user.repository.UserRepository
import com.susuhan.travelpick.global.kakao.client.KakaoApiClient
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.random.Random

class AuthCommandServiceTest : BehaviorSpec({

    val kakaoApiClient = mockk<KakaoApiClient>()
    val jwtTokenService = mockk<JwtTokenService>()
    val userRepository = mockk<UserRepository>()

    val sut = AuthCommandService(kakaoApiClient, jwtTokenService, userRepository)

    Given("카카오 로그인이 처음인 사용자가") {
        val request = KakaoLoginRequest("kakao-access-token")
        val kakaoAccessToken = "Bearer ${request.kakaoAccessToken}"
        val kakaoUserInfo = KakaoUserInfo(Random.nextInt().toString(), null, null, null)
        val user = kakaoUserInfo.toEntity()
        val tokenResponse = TokenResponse("access-token", "refresh-token")

        every { kakaoApiClient.getUserInfo(kakaoAccessToken) } returns kakaoUserInfo
        every { userRepository.findNotDeletedUserBySocialId(kakaoUserInfo.id) } returns null
        every { userRepository.save(any()) } returns user
        every { jwtTokenService.createJwtTokens(user) } returns tokenResponse

        When("카카오 로그인을 요청하면") {
            val result = sut.login(request)

            Then("회원 가입을 진행한 후") {
                verify(exactly = 1) { kakaoApiClient.getUserInfo(kakaoAccessToken) }
                verify(exactly = 1) { userRepository.findNotDeletedUserBySocialId(kakaoUserInfo.id) }
                verify(exactly = 1) { userRepository.save(any()) }
                verify(exactly = 1) { jwtTokenService.createJwtTokens(user) }
            }
            Then("토큰을 생성해 반환한다.") {
                result.accessToken shouldBe tokenResponse.accessToken
                result.refreshToken shouldBe tokenResponse.refreshToken
            }
        }
    }

    Given("이전에 카카오 로그인을 했던 사용자가") {
        val request = KakaoLoginRequest("kakao-access-token2")
        val kakaoAccessToken = "Bearer ${request.kakaoAccessToken}"
        val kakaoUserInfo = KakaoUserInfo(Random.nextInt().toString(), null, null, null)
        val user = kakaoUserInfo.toEntity()
        val tokenResponse = TokenResponse("access-token", "refresh-token")

        every { kakaoApiClient.getUserInfo(kakaoAccessToken) } returns kakaoUserInfo
        every { userRepository.findNotDeletedUserBySocialId(kakaoUserInfo.id) } returns user
        every { jwtTokenService.createJwtTokens(user) } returns tokenResponse

        When("카카오 로그인을 요청하면") {
            val result = sut.login(request)

            Then("로그인을 진행한 후") {
                verify(exactly = 1) { kakaoApiClient.getUserInfo(kakaoAccessToken) }
                verify(exactly = 1) { userRepository.findNotDeletedUserBySocialId(kakaoUserInfo.id) }
                verify(exactly = 0) { userRepository.save(kakaoUserInfo.toEntity()) }
                verify(exactly = 1) { jwtTokenService.createJwtTokens(user) }
            }
            Then("토큰을 생성해 반환한다.") {
                result.accessToken shouldBe tokenResponse.accessToken
                result.refreshToken shouldBe tokenResponse.refreshToken
            }
        }
    }
})
