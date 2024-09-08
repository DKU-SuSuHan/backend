package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelLeaderRequiredException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
class TravelMateQueryServiceTest : BehaviorSpec({

    val travelMateRepository = mockk<TravelMateRepository>()
    val travelRepository = mockk<TravelRepository>()

    val sut = TravelMateQueryService(travelMateRepository, travelRepository)

    Given("여행 주도자인 사용자로부터 여행 PK가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { 1L }
        }
        val travelMateList = List(5) { KotlinFixture.fixture<TravelMate>() }

        every { travelRepository.findLeaderId(travel.id) } returns travel.leaderId
        every { travelMateRepository.findAllParticipantMate(travel.id) } returns travelMateList

        When("여행 메이트 목록 조회를 요청하면") {
            val result = sut.getParticipantMateList(userId, travel.id)

            Then("DB에서 여행 메이트를 조회해") {
                verify(exactly = 1) { travelRepository.findLeaderId(travel.id) }
                verify(exactly = 1) { travelMateRepository.findAllParticipantMate(travel.id) }
            }
            Then("조회한 데이터 목록을 반환해야 한다.") {
                result.size shouldBe travelMateList.size
                result.map { it.id } shouldBe travelMateList.map { it.id }
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 여행 PK가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { 2L }
        }

        every { travelRepository.findLeaderId(travel.id) } returns travel.leaderId

        When("여행 메이트 목록 조회를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.getParticipantMateList(userId, travel.id)
                }
            }
        }
    }
})
