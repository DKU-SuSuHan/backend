package com.susuhan.travelpick.domain.user.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class UserQueryServiceTest : BehaviorSpec({

    val userRepository = mockk<UserRepository>()

    val sut = UserQueryService(userRepository)

    Given("DB에 존재하는 사용자의 PK로") {
        val user = KotlinFixture.fixture<User>()

        every { userRepository.findNotDeletedUser(user.id) } returns user

        When("사용자 조회를 요청하면") {
            val result = sut.getUserById(user.id)

            Then("DB를 조회해") {
                verify(exactly = 1) { userRepository.findNotDeletedUser(user.id) }
            }
            Then("사용자 데이터를 반환해야 한다.") {
                result shouldBe user
            }
        }
    }

    Given("DB에 존재하지 않은 사용자의 PK로") {
        val userId = 1L

        every { userRepository.findNotDeletedUser(userId) } returns null

        When("사용자 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<UserIdNotFoundException> {
                    sut.getUserById(userId)
                }
            }
        }
    }

    Given("DB에 존재하는 닉네임이 주어졌을 때") {
        val nickname = "nickname"

        every { userRepository.existsNotDeletedUserByNickname(nickname) } returns true

        When("중복 조회를 요청하면") {
            val result = sut.checkNicknameDuplicated(nickname)

            Then("DB를 조회해") {
                verify(exactly = 1) { userRepository.existsNotDeletedUserByNickname(nickname) }
            }
            Then("true를 반환해야 한다.") {
                result.isDuplicated shouldBe true
            }
        }
    }

    Given("DB에 존재하지 않은 닉네임이 주어졌을 때") {
        val nickname = "nickname"

        every { userRepository.existsNotDeletedUserByNickname(nickname) } returns false

        When("중복 조회를 요청하면") {
            val result = sut.checkNicknameDuplicated(nickname)

            Then("DB를 조회해") {
                verify(exactly = 1) { userRepository.existsNotDeletedUserByNickname(nickname) }
            }
            Then("false를 반환해야 한다.") {
                result.isDuplicated shouldBe false
            }
        }
    }

    Given("DB에 존재하는 사용자의 닉네임으로") {
        val user = KotlinFixture.fixture<User>()
        val nickname = "nickname"

        every { userRepository.findNotDeletedUserByNickname(nickname) } returns user

        When("사용자 조회를 요청하면") {
            val result = sut.searchByNickname(nickname)

            Then("DB를 조회해") {
                verify(exactly = 1) { userRepository.findNotDeletedUserByNickname(nickname) }
            }
            Then("사용자 데이터를 반환해야 한다.") {
                result.id shouldBe user.id
                result.nickname shouldBe user.nickname
                result.profileImageUrl shouldBe user.profileImageUrl
            }
        }
    }

    Given("DB에 존재하지 않은 사용자의 닉네임으로") {
        val nickname = "nickname"

        every { userRepository.findNotDeletedUserByNickname(nickname) } returns null

        When("사용자 조회를 요청하면") {
            val result = sut.searchByNickname(nickname)

            Then("DB를 조회해") {
                verify(exactly = 1) { userRepository.findNotDeletedUserByNickname(nickname) }
            }
            Then("null 값을 반환해야 한다.") {
                result.id shouldBe null
                result.nickname shouldBe null
                result.profileImageUrl shouldBe null
            }
        }
    }

    Given("로그인한 사용자가 DB에 존재하는 사용자 PK로") {
        val user = KotlinFixture.fixture<User>()

        every { userRepository.findNotDeletedUser(user.id) } returns user

        When("자신의 프로필 조회를 요청하면") {
            val result = sut.getLoginUserInfo(user.id)

            Then("DB를 조회해") {
                verify(exactly = 1) { userRepository.findNotDeletedUser(user.id) }
            }
            Then("사용자 데이터를 반환해야 한다.") {
                result.id shouldBe user.id
                result.nickname shouldBe user.nickname
                result.profileImageUrl shouldBe user.profileImageUrl
            }
        }
    }

    Given("로그인한 사용자가 DB에 존재하지 않은 사용자 PK로") {
        val userId = 1L

        every { userRepository.findNotDeletedUser(userId) } returns null

        When("자신의 프로필 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<UserIdNotFoundException> {
                    sut.getLoginUserInfo(userId)
                }
            }
        }
    }
})
