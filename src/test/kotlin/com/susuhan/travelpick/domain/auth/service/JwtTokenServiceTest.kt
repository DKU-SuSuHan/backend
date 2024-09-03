package com.susuhan.travelpick.domain.auth.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.auth.dto.request.RenewalTokensRequest
import com.susuhan.travelpick.domain.auth.repository.RefreshTokenRedisRepository
import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.domain.user.service.UserQueryService
import com.susuhan.travelpick.global.security.JwtTokenProvider
import com.susuhan.travelpick.global.security.exception.TokenNotValidException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify

class JwtTokenServiceTest : BehaviorSpec({

    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val userQueryService = mockk<UserQueryService>()
    val refreshTokenRedisRepository = mockk<RefreshTokenRedisRepository>()

    val sut = JwtTokenService(jwtTokenProvider, userQueryService, refreshTokenRedisRepository)

    Given("유저 정보가 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val accessToken = "access-token"
        val refreshToken = "refresh-token"

        every { jwtTokenProvider.createAccessToken(user.id, user.role) } returns accessToken
        every { jwtTokenProvider.createRefreshToken(user.id, user.role) } returns refreshToken
        every { refreshTokenRedisRepository.save(user.id, refreshToken) } just runs

        When("토큰 생성을 요청하면") {
            val result = sut.createJwtTokens(user)

            Then("액세스 토큰과 리프레시 토큰을 생성 후 리프레시 토큰은 redis에 저장하고") {
                verify(exactly = 1) { jwtTokenProvider.createAccessToken(user.id, user.role) }
                verify(exactly = 1) { jwtTokenProvider.createRefreshToken(user.id, user.role) }
                verify(exactly = 1) { refreshTokenRedisRepository.save(user.id, refreshToken) }
            }
            Then("생성한 토큰을 반환해야 한다.") {
                result.accessToken shouldBe accessToken
                result.refreshToken shouldBe refreshToken
            }
        }
    }

    Given("유효한 리프레시 토큰이 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val request = KotlinFixture.fixture<RenewalTokensRequest>()
        val newAccessToken = "new-access-token"
        val newRefreshToken = "new-refresh-token"

        every { jwtTokenProvider.getUserId(request.refreshToken) } returns user.id.toString()
        every { refreshTokenRedisRepository.findRefreshToken(user.id.toString()) } returns request.refreshToken
        every { jwtTokenProvider.createAccessToken(user.id, user.role) } returns newAccessToken
        every { jwtTokenProvider.createRefreshToken(user.id, user.role) } returns newRefreshToken
        every { refreshTokenRedisRepository.save(user.id, newRefreshToken) } just runs
        every { userQueryService.getUserById(user.id) } returns user

        When("토큰 갱신을 요청하면") {
            val result = sut.renewalJwtTokens(request)

            Then("액세스 토큰과 리프레시 토큰을 생성 후 리프레시 토큰은 redis에 저장하고") {
                verify(exactly = 1) { jwtTokenProvider.getUserId(request.refreshToken) }
                verify(exactly = 1) { refreshTokenRedisRepository.findRefreshToken(user.id.toString()) }
                verify(exactly = 1) { jwtTokenProvider.createAccessToken(user.id, user.role) }
                verify(exactly = 1) { jwtTokenProvider.createRefreshToken(user.id, user.role) }
                verify(exactly = 1) { refreshTokenRedisRepository.save(user.id, newRefreshToken) }
                verify(exactly = 1) { userQueryService.getUserById(user.id) }
            }
            Then("생성한 토큰을 반환해야 한다.") {
                result.accessToken shouldBe newAccessToken
                result.refreshToken shouldBe newRefreshToken
            }
        }
    }

    Given("유효하지 않은 리프레시 토큰이 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val request = KotlinFixture.fixture<RenewalTokensRequest>()

        every { jwtTokenProvider.getUserId(request.refreshToken) } returns user.id.toString()
        every { refreshTokenRedisRepository.findRefreshToken(user.id.toString()) } returns "xx"

        When("토큰 갱신을 요청하면") {
            Then("예외가 발생해야 한다.") {
                shouldThrow<TokenNotValidException> {
                    sut.renewalJwtTokens(request)
                }
            }
        }
    }

    Given("존재하지 않은 리프레시 토큰이 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val request = KotlinFixture.fixture<RenewalTokensRequest>()

        every { jwtTokenProvider.getUserId(request.refreshToken) } returns user.id.toString()
        every { refreshTokenRedisRepository.findRefreshToken(user.id.toString()) } returns null

        When("토큰 갱신을 요청하면") {
            Then("예외가 발생해야 한다.") {
                shouldThrow<TokenNotValidException> {
                    sut.renewalJwtTokens(request)
                }
            }
        }
    }
})
