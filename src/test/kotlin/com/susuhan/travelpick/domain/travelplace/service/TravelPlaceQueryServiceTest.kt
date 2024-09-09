package com.susuhan.travelpick.domain.travelplace.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelplace.dto.AddressInfo
import com.susuhan.travelpick.domain.travelplace.dto.TravelPlaceDto
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class TravelPlaceQueryServiceTest : BehaviorSpec({

    val travelPlaceRepository = mockk<TravelPlaceRepository>()
    val travelRepository = mockk<TravelRepository>()
    val travelMateRepository = mockk<TravelMateRepository>()

    val sut = TravelPlaceQueryService(travelPlaceRepository, travelRepository, travelMateRepository)

    Given("요청을 보낸 사용자가 여행 메이트로 존재하는 여행 PK와 특정 날짜가 주어졌을 때") {
        val userId = 1L
        val travelDay = 2
        val oneDayBudget = 50000L
        val travel = KotlinFixture.fixture<Travel>()
        val travelPlaceList = List(5) { KotlinFixture.fixture<TravelPlace>() }
        val travelPlaceDtoList = travelPlaceList.map { TravelPlaceDto.from(it) }
        val travelDate = travel.startAt.plusDays(travelDay.toLong() - 1)

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelMateRepository.existsNotDeletedMate(userId, travel.id) } returns true
        every { travelPlaceRepository.findConfirmPlaceListForDay(travel.id, travelDay) } returns travelPlaceList
        every { travelPlaceRepository.findOneDayBudget(travel.id, travelDay) } returns oneDayBudget

        When("확정된 여행 장소 목록 조회를 요청하면") {
            val result = sut.getConfirmPlaceList(userId, travel.id, travelDay)

            Then("DB에서 데이터를 조회해") {
                verify(exactly = 1) { travelRepository.findNotDeletedPlannedTravel(travel.id) }
                verify(exactly = 1) { travelMateRepository.existsNotDeletedMate(userId, travel.id) }
                verify(exactly = 1) { travelPlaceRepository.findConfirmPlaceListForDay(travel.id, travelDay) }
                verify(exactly = 1) { travelPlaceRepository.findOneDayBudget(travel.id, travelDay) }
            }
            Then("조회한 데이터를 반환해야 한다.") {
                result.travelId shouldBe travel.id
                result.travelDate shouldBe travelDate
                result.travelPlaceList shouldBe travelPlaceDtoList
                result.oneDayBudget shouldBe oneDayBudget
            }
        }
    }

    Given("DB에 존재하지 않은 여행 PK와 특정 날짜가 주어졌을 때") {
        val userId = 1L
        val travelId = 1L
        val travelDay = 2

        every { travelRepository.findNotDeletedPlannedTravel(travelId) } returns null

        When("확정된 여행 장소 목록 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.getConfirmPlaceList(userId, travelId, travelDay)
                }
            }
        }
    }

    Given("요청을 보낸 사용자가 조회하고자 하는 여행의 메이트가 아닌 데이터가 주어졌을 때") {
        val userId = 1L
        val travelDay = 2
        val travel = KotlinFixture.fixture<Travel>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelMateRepository.existsNotDeletedMate(userId, travel.id) } returns false

        When("확정된 여행 장소 목록 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelMateNotFoundException> {
                    sut.getConfirmPlaceList(userId, travel.id, travelDay)
                }
            }
        }
    }

    Given("요청을 보낸 사용자가 여행 메이트에 포함된 진행 중인 여행 PK가 주어졌을 때") {
        val userId = 1L
        val travelId = 1L
        val addressInfoList = List(5) { KotlinFixture.fixture<AddressInfo>() }

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns true
        every { travelMateRepository.existsNotDeletedMate(userId, travelId) } returns true
        every { travelPlaceRepository.findPostcodeAndAddress(travelId) } returns addressInfoList

        When("모든 일정의 도로명 주소 조회를 요청하면") {
            val result = sut.getAllConfirmPlaceAddressList(userId, travelId)

            Then("DB에서 데이터 목록을 조회해") {
                verify(exactly = 1) { travelRepository.existsNotDeletedPlannedTravel(travelId) }
                verify(exactly = 1) { travelMateRepository.existsNotDeletedMate(userId, travelId) }
                verify(exactly = 1) { travelPlaceRepository.findPostcodeAndAddress(travelId) }
            }
            Then("조회한 데이터 목록을 반환해야 한다.") {
                result.size shouldBe addressInfoList.size
                result.map { it.id } shouldBe addressInfoList.map { it.id }
            }
        }
    }

    Given("DB에 존재하지 않은 여행 PK가 주어졌을 때") {
        val userId = 1L
        val travelId = 1L

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns false

        When("모든 일정의 도로명 주소 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.getAllConfirmPlaceAddressList(userId, travelId)
                }
            }
        }
    }

    Given("요청을 보낸 사용자가 조회를 하고자 하는 여행의 메이트에 포함되지 않은 데이터가 주어졌을 때") {
        val userId = 1L
        val travelId = 1L

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns true
        every { travelMateRepository.existsNotDeletedMate(userId, travelId) } returns false

        When("모든 일정의 도로명 주소 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelMateNotFoundException> {
                    sut.getAllConfirmPlaceAddressList(userId, travelId)
                }
            }
        }
    }
})
