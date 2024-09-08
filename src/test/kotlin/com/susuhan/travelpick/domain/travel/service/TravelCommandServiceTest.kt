package com.susuhan.travelpick.domain.travel.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.travel.dto.request.TravelCreateRequest
import com.susuhan.travelpick.domain.travel.dto.request.TravelUpdateRequest
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.exception.TravelLeaderRequiredException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.service.TravelMateCommandService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.context.ActiveProfiles
import kotlin.random.Random

@ActiveProfiles("test")
class TravelCommandServiceTest : BehaviorSpec({

    val travelRepository = mockk<TravelRepository>()
    val travelMateCommandService = mockk<TravelMateCommandService>()

    val sut = TravelCommandService(travelRepository, travelMateCommandService)

    Given("새로운 여행 데이터가 주어졌을 때") {
        val userId = 1L
        val request = KotlinFixture.fixture<TravelCreateRequest>()
        val travel = KotlinFixture.fixture<Travel>()

        every { travelRepository.save(any()) } returns travel
        justRun { travelMateCommandService.createTravelLeader(userId, travel) }

        When("여행 저장을 요청하면") {
            val result = sut.create(userId, request)

            Then("DB에 여행 데이터를 저장하고") {
                verify(exactly = 1) { travelRepository.save(any()) }
                verify(exactly = 1) { travelMateCommandService.createTravelLeader(userId, travel) }
            }
            Then("저장한 여행 데이터를 반환해야 한다.") {
                result.id shouldBe travel.id
                result.title shouldBe travel.title
                result.theme shouldBe travel.theme
                result.sido shouldBe travel.address.sido
                result.sgg shouldBe travel.address.sgg
                result.startAt shouldBe travel.startAt
                result.endAt shouldBe travel.endAt
            }
        }
    }

    Given("DB에 존재하는 여행에 대한 데이터가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
        }
        val request = KotlinFixture.fixture<TravelUpdateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel

        When("여행 데이터 수정을 요청하면") {
            val result = sut.update(userId, travel.id, request)

            Then("DB에서 여행 데이터를 조회해 수정한 뒤") {
                verify(exactly = 1) { travelRepository.findNotDeletedPlannedTravel(travel.id) }
            }
            Then("수정한 데이터를 반환해야 한다.") {
                result.id shouldBe travel.id
                result.title shouldBe request.title
                result.theme shouldBe request.theme
                result.sido shouldBe request.sido
                result.sgg shouldBe request.sgg
                result.startAt shouldBe request.startAt
                result.endAt shouldBe request.endAt
            }
        }
    }

    Given("DB에 존재하지 않은 여행에 대한 데이터가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
        }
        val request = KotlinFixture.fixture<TravelUpdateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns null

        When("여행 데이터 수정을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.update(userId, travel.id, request)
                }
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 여행 데이터가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { Random.nextLong() }
        }
        val request = KotlinFixture.fixture<TravelUpdateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel

        When("여행 데이터 수정을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.update(userId, travel.id, request)
                }
            }
        }
    }

    Given("DB에 존재하는 여행 PK가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
        }

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        justRun { travelMateCommandService.softDeleteAll(travel.id) }

        When("여행 데이터 삭제을 요청하면") {
            val result = sut.softDelete(userId, travel.id)

            Then("DB에서 여행 데이터를 조회 후 삭제 표시를 해") {
                verify(exactly = 1) { travelRepository.findNotDeletedPlannedTravel(travel.id) }
                verify(exactly = 1) { travelMateCommandService.softDeleteAll(travel.id) }
            }
            Then("여행의 deleteAt이 null이 아니여야 하며") {
                travel.deleteAt shouldNotBe null
            }
            Then("반환 값이 없어야 한다.") {
                result shouldBe Unit
            }
        }
    }

    Given("DB에 존재하지 않은 여행 PK가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
        }

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns null

        When("여행 데이터 삭제을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.softDelete(userId, travel.id)
                }
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 여행 PK가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { Random.nextLong() }
        }

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel

        When("여행 데이터 삭제을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.softDelete(userId, travel.id)
                }
            }
        }
    }
})
