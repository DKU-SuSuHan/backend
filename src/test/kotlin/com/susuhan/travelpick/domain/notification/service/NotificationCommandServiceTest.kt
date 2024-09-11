package com.susuhan.travelpick.domain.notification.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.notification.entity.Notification
import com.susuhan.travelpick.domain.notification.repository.NotificationRepository
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import com.susuhan.travelpick.global.event.TravelAction
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class NotificationCommandServiceTest : BehaviorSpec({

    val notificationRepository = mockk<NotificationRepository>()
    val travelRepository = mockk<TravelRepository>()
    val travelMateRepository = mockk<TravelMateRepository>()
    val userRepository = mockk<UserRepository>()

    val sut = NotificationCommandService(notificationRepository, travelRepository, travelMateRepository, userRepository)

    Given("알림 메시지를 생성하는 이벤트가 발생되었을 때") {
        val travelId = 1L
        val userIdList = listOf(1L, 3L, 5L)
        val user = KotlinFixture.fixture<User>()
        val travelMate = KotlinFixture.fixture<TravelMate>()
        val notificationList = List(userIdList.size) { KotlinFixture.fixture<Notification>() }

        every { userRepository.findNotDeletedUser(user.id) } returns user
        every { travelMateRepository.findAllUserId(travelId) } returns userIdList
        every { notificationRepository.saveAll(any()) } returns notificationList

        When("알림 메시지 생성을 요청하면") {
            val result = sut.createNotification(user.id, travelId, TravelAction.ADD_MATE, null, travelMate)

            Then("여행 그룹의 모든 여행 메이트를 조회해 알림 객체를 생성하고") {
                verify(exactly = 1) { userRepository.findNotDeletedUser(user.id) }
                verify(exactly = 1) { travelMateRepository.findAllUserId(travelId) }
                verify(exactly = 1) { notificationRepository.saveAll(any()) }
            }
            Then("아무 값도 반환하지 않아야 한다.") {
                result shouldBe Unit
            }
        }
    }

    Given("DB에 존재하지 않은 메시지를 보내는 사용자 데이터가 주어진 경우") {
        val userId = 1L
        val travelId = 1L
        val travelMate = KotlinFixture.fixture<TravelMate>()

        every { userRepository.findNotDeletedUser(userId) } returns null

        When("알림 메시지 생성을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<UserIdNotFoundException> {
                    sut.createNotification(userId, travelId, TravelAction.ADD_MATE, null, travelMate)
                }
            }
        }
    }

    Given("DB에 존재하는 여행 그룹에 속한 여행 메이트 데이터가 주어졌을 때") {
        val userId = 1L
        val travelId = 1L
        val notificationList = List(5) { KotlinFixture.fixture<Notification>() }

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns true
        every { travelMateRepository.existsNotDeletedMate(userId, travelId) } returns true
        every { notificationRepository.findNotifications(userId, travelId) } returns notificationList

        When("그룹 메시지 목록 조회를 요청하면") {
            val result = sut.getGroupMessageList(userId, travelId)

            Then("DB에서 메시지를 조회해") {
                verify(exactly = 1) { travelRepository.existsNotDeletedPlannedTravel(travelId) }
                verify(exactly = 1) { travelMateRepository.existsNotDeletedMate(userId, travelId) }
                verify(exactly = 1) { notificationRepository.findNotifications(userId, travelId) }
            }
            Then("조회한 데이터를 반환해야 한다.") {
                result.size shouldBe notificationList.size
                result.map { it.id } shouldBe notificationList.map { it.id }
            }
        }
    }

    Given("DB에 존재하지 않은 여행이 주어졌을 때") {
        val userId = 1L
        val travelId = 1L

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns false

        When("그룹 메시지 목록 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.getGroupMessageList(userId, travelId)
                }
            }
        }
    }

    Given("요청을 보낸 사용자가 여행 그룹의 여행 메이트가 아닌 경우") {
        val userId = 1L
        val travelId = 1L

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns true
        every { travelMateRepository.existsNotDeletedMate(userId, travelId) } returns false

        When("그룹 메시지 목록 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelMateNotFoundException> {
                    sut.getGroupMessageList(userId, travelId)
                }
            }
        }
    }
})
