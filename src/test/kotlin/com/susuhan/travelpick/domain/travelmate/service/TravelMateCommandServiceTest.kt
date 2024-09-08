package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.exception.TravelLeaderRequiredException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.dto.request.LeaderDelegateRequest
import com.susuhan.travelpick.domain.travelmate.dto.request.TravelMateCreateRequest
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.travelmate.exception.TravelLeaderDeletionException
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.user.entity.User
import com.susuhan.travelpick.domain.user.exception.UserIdNotFoundException
import com.susuhan.travelpick.domain.user.repository.UserRepository
import com.susuhan.travelpick.global.event.TravelEvent
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.springframework.context.ApplicationEventPublisher
import org.springframework.test.context.ActiveProfiles
import kotlin.random.Random

@ActiveProfiles("test")
class TravelMateCommandServiceTest : BehaviorSpec({

    val travelMateRepository = mockk<TravelMateRepository>()
    val travelRepository = mockk<TravelRepository>()
    val userRepository = mockk<UserRepository>()
    val eventListener = mockk<ApplicationEventPublisher>()

    val sut = TravelMateCommandService(travelMateRepository, travelRepository, userRepository, eventListener)

    Given("여행 메이트 관련된 데이터가 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val travel = KotlinFixture.fixture<Travel>()
        val travelMate = KotlinFixture.fixture<TravelMate>()

        every { userRepository.findNotDeletedUser(user.id) } returns user
        every { travelMateRepository.save(any()) } returns travelMate

        When("여행 주도자 생성을 요청하면") {
            val result = sut.createTravelLeader(user.id, travel)

            Then("DB에 여행 메이트 데이터를 저장하고") {
                verify(exactly = 1) { userRepository.findNotDeletedUser(user.id) }
                verify(exactly = 1) { travelMateRepository.save(any()) }
            }
            Then("아무 값도 반환하지 않아야 한다.") {
                result shouldBe Unit
            }
        }
    }

    Given("DB에 존재하지 않은 사용자가 여행 메이트 데이터로 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val travel = KotlinFixture.fixture<Travel>()

        every { userRepository.findNotDeletedUser(user.id) } returns null

        When("여행 주도자 생성을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<UserIdNotFoundException> {
                    sut.createTravelLeader(user.id, travel)
                }
            }
        }
    }

    Given("여행 메이트로 생성하고자 하는 사용자 PK 목록이 주어졌을 때") {
        val leaderId = 1L
        val users = (15L..18L).map { index ->
            KotlinFixture.fixture<User> {
                property(User::id) { index }
            }
        }
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { leaderId }
        }
        val userIdSet = setOf(users[0].id, users[1].id, users[2].id)
        val request = TravelMateCreateRequest(userIdSet)
        val travelMate = request.toEntity(users[0], travel)
        val travelMate2 = request.toEntity(users[1], travel)

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { userRepository.findAllNotDeletedUserById(request.userIds) } returns listOf(users[0], users[1], users[2])
        every { travelMateRepository.existsNotDeletedMate(users[0].id, travel.id) } returns false
        every { travelMateRepository.existsNotDeletedMate(users[1].id, travel.id) } returns false
        every { travelMateRepository.existsNotDeletedMate(users[2].id, travel.id) } returns true
        every { travelMateRepository.saveAll(any()) } returns listOf(travelMate, travelMate2)
        justRun { eventListener.publishEvent(any<TravelEvent>()) }

        When("여행 참여자들의 생성을 요청하면") {
            val result = sut.createTravelParticipants(leaderId, travel.id, request)

            Then("여행을 조회하고 이전에 삭제된 여행 메이트인지 확인한 뒤 여행 메이트를 생성한 후") {
                verify(exactly = 1) { travelRepository.findNotDeletedPlannedTravel(travel.id) }
                verify(exactly = 1) { userRepository.findAllNotDeletedUserById(userIdSet) }
                verify(exactly = 1) { travelMateRepository.existsNotDeletedMate(users[0].id, travel.id) }
                verify(exactly = 1) { travelMateRepository.existsNotDeletedMate(users[1].id, travel.id) }
                verify(exactly = 1) { travelMateRepository.existsNotDeletedMate(users[2].id, travel.id) }
                verify(exactly = 1) { travelMateRepository.saveAll(any()) }
                verify(exactly = 2) { eventListener.publishEvent(any<TravelEvent>()) }
            }
            Then("생성한 데이터를 반환해야 한다.") {
                result.size shouldBe 2
            }
        }
    }

    Given("DB에 존재하지 않은 여행에 대한 데이터가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel>()
        val request = KotlinFixture.fixture<TravelMateCreateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns null

        When("여행 참여자들의 생성을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.createTravelParticipants(userId, travel.id, request)
                }
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 여행 메이트 생성 데이터가 주어졌을 때") {
        val user = KotlinFixture.fixture<User>()
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { Random.nextLong() }
        }
        val request = KotlinFixture.fixture<TravelMateCreateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel

        When("여행 참여자들의 생성을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.createTravelParticipants(user.id, travel.id, request)
                }
            }
        }
    }

    Given("여행 주도자인 사용자로부터 삭제한 여행 메이트의 PK가 주어졌을 때") {
        val userId = 1L
        val leaderId = 1L
        val travelId = 1L
        val travelMate = KotlinFixture.fixture<TravelMate> {
            property(TravelMate::groupRole) { GroupRole.PARTICIPANT }
        }

        every { travelRepository.findLeaderId(travelId) } returns leaderId
        every { travelMateRepository.findNotDeletedMate(travelMate.id) } returns travelMate
        justRun { eventListener.publishEvent(any<TravelEvent>()) }

        When("여행 메이트의 삭제를 요청하면") {
            val result = sut.softDelete(userId, travelId, travelMate.id)

            Then("여행 메이트를 조회 후 삭제 표시를 해") {
                verify(exactly = 1) { travelRepository.findLeaderId(travelId) }
                verify(exactly = 1) { travelMateRepository.findNotDeletedMate(travelMate.id) }
                verify(exactly = 1) { eventListener.publishEvent(any<TravelEvent>()) }
            }
            Then("여행 메이트의 deleteAt이 null이 아니여야 하며") {
                travelMate.deleteAt shouldNotBe null
            }
            Then("아무 값도 반환하지 않아야 한다.") {
                result shouldBe Unit
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 여행 삭제 데이터가 주어졌을 때") {
        val userId = 1L
        val leaderId = 2L
        val travelId = 1L
        val travelMateId = 1L

        every { travelRepository.findLeaderId(travelId) } returns leaderId

        When("여행 메이트의 삭제를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.softDelete(userId, travelId, travelMateId)
                }
            }
        }
    }

    Given("DB에 존재하지 않은 여행 메이트를 삭제하는 데이터가 주어졌을 때") {
        val userId = 1L
        val leaderId = 1L
        val travelId = 1L
        val travelMateId = 1L

        every { travelRepository.findLeaderId(travelId) } returns leaderId
        every { travelMateRepository.findNotDeletedMate(travelMateId) } returns null

        When("여행 메이트의 삭제를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelMateIdNotFoundException> {
                    sut.softDelete(userId, travelId, travelMateId)
                }
            }
        }
    }

    Given("여행 주도자인 메이트에 대한 삭제 데이터가 주어졌을 때") {
        val userId = 1L
        val leaderId = 1L
        val travelId = 1L
        val travelMate = KotlinFixture.fixture<TravelMate> {
            property(TravelMate::groupRole) { GroupRole.LEADER }
        }

        every { travelRepository.findLeaderId(travelId) } returns leaderId
        every { travelMateRepository.findNotDeletedMate(travelMate.id) } returns travelMate

        When("여행 메이트의 삭제를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderDeletionException> {
                    sut.softDelete(userId, travelId, travelMate.id)
                }
            }
        }
    }

    Given("여행 주도자인 사용자로부터 권한 위임을 할 여행 메이트의 PK가 주어졌을 때") {
        val user = KotlinFixture.fixture<User> {
            property(User::id) { 1L }
        }
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { user.id }
        }
        val request = LeaderDelegateRequest(3L)
        val leader = KotlinFixture.fixture<TravelMate> {
            property(TravelMate::user) { user }
        }
        val participant = KotlinFixture.fixture<TravelMate>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelMateRepository.findNotDeletedMateByUser(travel.id, user.id) } returns leader
        every { travelMateRepository.findNotDeletedMate(request.travelMateId) } returns participant
        justRun { eventListener.publishEvent(any<TravelEvent>()) }

        When("여행 주도자 권한 위임을 요청하면") {
            val result = sut.delegateLeaderRole(user.id, travel.id, request)

            Then("요청한 사용자가 여행 주도자인지 확인한 뒤 권한을 위임해") {
                verify(exactly = 1) { travelRepository.findNotDeletedPlannedTravel(travel.id) }
                verify(exactly = 1) { travelMateRepository.findNotDeletedMateByUser(travel.id, user.id) }
                verify(exactly = 1) { travelMateRepository.findNotDeletedMate(request.travelMateId) }
                verify(exactly = 1) { eventListener.publishEvent(any<TravelEvent>()) }
            }
            Then("서로의 권한이 바뀌어야 하며") {
                leader.groupRole shouldBe GroupRole.PARTICIPANT
                participant.groupRole shouldBe GroupRole.LEADER
            }
            Then("여행 PK를 반환해야 한다.") {
                result.travelId shouldBe travel.id
            }
        }
    }

    Given("DB에 존재하지 않은 여행지에 대한 권한 위임 데이터가 주어졌을 때") {
        val userId = 1L
        val travelId = 1L
        val request = KotlinFixture.fixture<LeaderDelegateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travelId) } returns null

        When("여행 주도자 권한 위임을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.delegateLeaderRole(userId, travelId, request)
                }
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 여행 권한 위임 데이터가 주어졌을 때") {
        val user = KotlinFixture.fixture<User> {
            property(User::id) { 1L }
        }
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { Random.nextLong() }
        }
        val request = KotlinFixture.fixture<LeaderDelegateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel

        When("여행 주도자 권한 위임을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.delegateLeaderRole(user.id, travel.id, request)
                }
            }
        }
    }

    Given("DB에 존재하지 않은 여행 주도자 메이트에 대한 데이터가 주어졌을 때") {
        val user = KotlinFixture.fixture<User> {
            property(User::id) { 1L }
        }
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { user.id }
        }
        val request = LeaderDelegateRequest(3L)

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelMateRepository.findNotDeletedMateByUser(travel.id, user.id) } returns null

        When("여행 주도자 권한 위임을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelMateIdNotFoundException> {
                    sut.delegateLeaderRole(user.id, travel.id, request)
                }
            }
        }
    }

    Given("DB에 존재하지 않은 여행 메이트에게 권한 위임을 하는 데이터가 주어졌을 때") {
        val user = KotlinFixture.fixture<User> {
            property(User::id) { 1L }
        }
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { user.id }
        }
        val request = LeaderDelegateRequest(3L)
        val leader = KotlinFixture.fixture<TravelMate> {
            property(TravelMate::user) { user }
        }

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelMateRepository.findNotDeletedMateByUser(travel.id, user.id) } returns leader
        every { travelMateRepository.findNotDeletedMate(request.travelMateId) } returns null

        When("여행 주도자 권한 위임을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelMateIdNotFoundException> {
                    sut.delegateLeaderRole(user.id, travel.id, request)
                }
            }
        }
    }
})
