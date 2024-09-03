package com.susuhan.travelpick.domain.user.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.user.dto.request.NicknameUpdateRequest
import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserCommandServiceTest : BehaviorSpec({

    val userRepository = mockk<UserRepository>()

    val sut = UserCommandService(userRepository)

    Given("DB에 존재하는 사용자의 PK와 새로운 닉네임이 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val request = KotlinFixture.fixture<NicknameUpdateRequest>()
        val newNickname = request.nickname

        every { userRepository.findNotDeletedUser(user.id) } returns user

        When("닉네임 수정을 요청하면") {
            val result = sut.updateNickname(user.id, request)

            Then("DB에서 요청한 사용자를 조회해") {
                verify(exactly = 1) { userRepository.findNotDeletedUser(user.id) }
            }
            Then("닉네임을 수정 후 수정된 데이터를 반환해야 한다.") {
                result.nickname shouldBe newNickname
                result.email shouldBe user.email
                result.profileImageUrl shouldBe user.profileImageUrl
            }
        }
    }

    Given("DB에 존재하지 않은 사용자의 PK와 새로운 닉네임이 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val request = KotlinFixture.fixture<NicknameUpdateRequest>()

        every { userRepository.findNotDeletedUser(user.id) } returns null

        When("닉네임 수정을 요청하면") {
            Then("예외가 발생해야 한다.") {
                shouldThrow<UserIdNotFoundException> {
                    sut.updateNickname(user.id, request)
                }
            }
        }
    }
})
