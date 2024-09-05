package com.susuhan.travelpick.domain.travel.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.travel.dto.response.MyTravelInfoResponse
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class TravelQueryServiceTest : BehaviorSpec({

    val travelMateRepository = mockk<TravelMateRepository>()
    val travelPlaceRepository = mockk<TravelPlaceRepository>()

    val sut = TravelQueryService(travelMateRepository, travelPlaceRepository)

    Given("요청한 사용자가 참여 중인 여행의 PK가 주어졌을 때") {
        val userId = 1L
        val totalBudget = 250000L
        val travel = KotlinFixture.fixture<Travel>()
        val travelMate = KotlinFixture.fixture<TravelMate>()
        val expectResult = MyTravelInfoResponse.from(travel, travelMate, totalBudget)

        every { travelMateRepository.findTravel(userId, travel.id) } returns travel
        every { travelMateRepository.findNotDeletedMateByUser(travel.id, userId) } returns travelMate
        every { travelPlaceRepository.findTotalBudget(travel.id) } returns totalBudget

        When("여행 데이터 조회를 요청하면") {
            val result = sut.getMyTravel(userId, travel.id)

            Then("DB에서 여행 데이터를 조회한 뒤") {
                verify(exactly = 1) { travelMateRepository.findTravel(userId, travel.id) }
                verify(exactly = 1) { travelMateRepository.findNotDeletedMateByUser(travel.id, userId) }
                verify(exactly = 1) { travelPlaceRepository.findTotalBudget(travel.id) }
            }
            Then("조회한 데이터를 반환한다.") {
                result shouldBe expectResult
            }
        }
    }

    Given("DB에 존재하지 않은 여행의 PK가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel>()

        every { travelMateRepository.findTravel(userId, travel.id) } returns null

        When("여행 데이터 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.getMyTravel(userId, travel.id)
                }
            }
        }
    }

    Given("요청한 사용자가 참여 중이지 않은 여행의 PK가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel>()

        every { travelMateRepository.findTravel(userId, travel.id) } returns travel
        every { travelMateRepository.findNotDeletedMateByUser(travel.id, userId) } returns null

        When("여행 데이터 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelMateIdNotFoundException> {
                    sut.getMyTravel(userId, travel.id)
                }
            }
        }
    }

    Given("요청을 보낸 사용자의 PK가 주어졌을 때") {
        val userId = 1L
        val travelList = List(5) { KotlinFixture.fixture<Travel>() }

        every { travelMateRepository.findPlannedTravel(userId) } returns travelList
        every { travelMateRepository.findEndedTravel(userId) } returns travelList

        When("참여 중인 여행 데이터 목록 조회를 요청하면") {
            val result = sut.getPlannedTravelList(userId)

            Then("DB에서 여행 데이터를 조회한 뒤") {
                verify(exactly = 1) { travelMateRepository.findPlannedTravel(userId) }
            }
            Then("조회한 데이터 목록을 반환한다.") {
                result.size shouldBe travelList.size
                result.map { it.id } shouldBe travelList.map { it.id }
            }
        }

        When("참여했던 여행 데이터 목록 조회를 요청하면") {
            val result = sut.getEndedTravelList(userId)

            Then("DB에서 여행 데이터를 조회한 뒤") {
                verify(exactly = 1) { travelMateRepository.findEndedTravel(userId) }
            }
            Then("조회한 데이터 목록을 반환한다.") {
                result.size shouldBe travelList.size
                result.map { it.id } shouldBe travelList.map { it.id }
            }
        }
    }
})
