package com.susuhan.travelpick.domain.travelplace.service

import com.appmattus.kotlinfixture.decorator.nullability.NeverNullStrategy
import com.susuhan.travelpick.KotlinFixture
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.exception.TravelLeaderRequiredException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceCreateRequest
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceUpdateRequest
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import com.susuhan.travelpick.domain.travelplace.exception.TravelDateNotValidException
import com.susuhan.travelpick.domain.travelplace.exception.TravelPlaceIdNotFoundException
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
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
import java.time.LocalDate
import java.time.Period
import kotlin.random.Random

@ActiveProfiles("test")
class TravelPlaceCommandServiceTest : BehaviorSpec({

    val travelPlaceRepository = mockk<TravelPlaceRepository>()
    val travelRepository = mockk<TravelRepository>()
    val eventListener = mockk<ApplicationEventPublisher>()

    val sut = TravelPlaceCommandService(travelPlaceRepository, travelRepository, eventListener)

    Given("여행에 추가할 여행 장소 데이터가 주어졌을 때") {
        val userId = 1L
        val placeTotalCount = 5L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
            property(Travel::startAt) { LocalDate.of(2025, 1, 1) }
            property(Travel::endAt) { LocalDate.of(2025, 1, 5) }
        }
        val request = KotlinFixture.fixture<TravelPlaceCreateRequest> {
            property(TravelPlaceCreateRequest::travelDate) { LocalDate.of(2025, 1, 2) }
        }
        val travelDay = Period.between(travel.startAt, request.travelDate).days + 1
        val travelPlace = KotlinFixture.fixture<TravelPlace>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelPlaceRepository.countPlaceTotalNumber(travel.id, travelDay) } returns placeTotalCount + 1
        every { travelPlaceRepository.save(any()) } returns travelPlace
        justRun { eventListener.publishEvent(any<TravelEvent>()) }

        When("여행 장소 추가를 요청하면") {
            val result = sut.create(userId, travel.id, request)

            Then("해당 여행이 존재하는지 확인하고 여행 장소를 생성한 뒤") {
                verify(exactly = 1) { travelRepository.findNotDeletedPlannedTravel(travel.id) }
                verify(exactly = 1) { travelPlaceRepository.countPlaceTotalNumber(travel.id, travelDay) }
                verify(exactly = 1) { travelPlaceRepository.save(any()) }
                verify(exactly = 1) { eventListener.publishEvent(any<TravelEvent>()) }
            }
            Then("생성된 여행 장소의 PK를 반환해야 한다.") {
                result.travelId shouldBe travel.id
            }
        }
    }

    Given("DB에 존재하지 않는 여행에 추가할 여행 장소 데이터가 주어졌을 때") {
        val userId = 1L
        val travelId = 1L
        val request = KotlinFixture.fixture<TravelPlaceCreateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travelId) } returns null

        When("여행 장소 추가를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.create(userId, travelId, request)
                }
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 여행에 추가할 여행 장소 데이터가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { Random.nextLong() }
        }
        val request = KotlinFixture.fixture<TravelPlaceCreateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel

        When("여행 장소 추가를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.create(userId, travel.id, request)
                }
            }
        }
    }

    Given("여행의 시작 날짜와 종료 날짜에 포함되지 않은 날짜로 장소를 추가하는 데이터가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
        }
        val request = KotlinFixture.fixture<TravelPlaceCreateRequest> {
            property(TravelPlaceCreateRequest::travelDate) { LocalDate.MIN }
        }

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel

        When("여행 장소 추가를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelDateNotValidException> {
                    sut.create(userId, travel.id, request)
                }
            }
        }
    }

    Given("이전에 추가된 여행 장소를 수정하는 데이터가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
            property(Travel::startAt) { LocalDate.of(2025, 1, 1) }
            property(Travel::endAt) { LocalDate.of(2025, 1, 5) }
        }
        val request = KotlinFixture.fixture<TravelPlaceUpdateRequest> {
            property(TravelPlaceUpdateRequest::travelDate) { LocalDate.of(2025, 1, 2) }
            property(TravelPlaceUpdateRequest::urlLink) { "test.com" }
        }
        val requestUrlLinkIsNull = KotlinFixture.fixture<TravelPlaceUpdateRequest> {
            property(TravelPlaceUpdateRequest::travelDate) { request.travelDate }
            property(TravelPlaceUpdateRequest::urlLink) { null }
        }
        val travelPlace = KotlinFixture.fixture<TravelPlace> { NeverNullStrategy }
        val travelDay = Period.between(travel.startAt, request.travelDate).days + 1

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) } returns travelPlace

        When("여행 장소 수정을 요청하면") {
            val result = sut.update(userId, travel.id, travelPlace.id, request)

            Then("DB에서 여행과 여행 장소를 조회해") {
                verify(exactly = 1) { travelRepository.findNotDeletedPlannedTravel(travel.id) }
                verify(exactly = 1) { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) }
            }
            Then("여행 장소 데이터를 변경 한 뒤") {
                travelPlace.travelDay shouldBe travelDay
                travelPlace.name shouldBe request.name
                travelPlace.postcode shouldBe request.postcode
                travelPlace.address shouldBe request.address
                travelPlace.budget shouldBe request.budget
                travelPlace.urlLink shouldBe request.urlLink
            }
            Then("변경된 데이터의 PK와 여행 날짜를 반환해야 한다.") {
                result.travelId shouldBe travel.id
                result.travelDay shouldBe travelPlace.travelDay
            }
        }

        When("urlLink가 null인 데이터로 여행 장소 수정을 요청하면") {
            val result = sut.update(userId, travel.id, travelPlace.id, requestUrlLinkIsNull)

            Then("DB에서 여행과 여행 장소를 조회해") {
                verify(exactly = 1) { travelRepository.findNotDeletedPlannedTravel(travel.id) }
                verify(exactly = 1) { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) }
            }
            Then("urlLink을 제외한 여행 장소 데이터를 변경 한 뒤") {
                travelPlace.travelDay shouldBe travelDay
                travelPlace.name shouldBe requestUrlLinkIsNull.name
                travelPlace.postcode shouldBe requestUrlLinkIsNull.postcode
                travelPlace.address shouldBe requestUrlLinkIsNull.address
                travelPlace.budget shouldBe requestUrlLinkIsNull.budget
                travelPlace.urlLink shouldBe travelPlace.urlLink
            }
            Then("변경된 데이터의 PK와 여행 날짜를 반환해야 한다.") {
                result.travelId shouldBe travel.id
                result.travelDay shouldBe travelPlace.travelDay
            }
        }
    }

    Given("DB에 존재하지 않은 여행에 대해 장소를 수정하는 데이터가 주어졌을 때") {
        val userId = 1L
        val travelId = 1L
        val travelPlaceId = 1L
        val request = KotlinFixture.fixture<TravelPlaceUpdateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travelId) } returns null

        When("여행 장소 수정을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.update(userId, travelId, travelPlaceId, request)
                }
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 수정하는 여행 장소 데이터가 주어졌을 때") {
        val userId = 1L
        val travelPlaceId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { Random.nextLong() }
        }
        val request = KotlinFixture.fixture<TravelPlaceUpdateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel

        When("여행 장소 수정을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.update(userId, travel.id, travelPlaceId, request)
                }
            }
        }
    }

    Given("DB에 존재하지 않은 여행 장소에 대해 수정하는 데이터가 주어졌을 때") {
        val userId = 1L
        val travelPlaceId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
        }
        val request = KotlinFixture.fixture<TravelPlaceUpdateRequest>()

        every { travelRepository.findNotDeletedPlannedTravel(travel.id) } returns travel
        every { travelPlaceRepository.findNotDeletedTravelPlace(travelPlaceId) } returns null

        When("여행 장소 수정을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelPlaceIdNotFoundException> {
                    sut.update(userId, travel.id, travelPlaceId, request)
                }
            }
        }
    }

    Given("이전에 추가된 여행 장소를 삭제하는 데이터가 주어졌을 때") {
        val userId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
        }
        val travelPlace = KotlinFixture.fixture<TravelPlace>()

        every { travelRepository.findLeaderId(travel.id) } returns travel.leaderId
        every { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) } returns travelPlace
        justRun { travelPlaceRepository.decrementAllSequence(travelPlace.sequence) }
        justRun { eventListener.publishEvent(any<TravelEvent>()) }

        When("여행 장소 삭제를 요청하면") {
            val result = sut.softDelete(userId, travel.id, travelPlace.id)

            Then("DB에서 여행 장소 데이터를 조회 후 삭제 표시를 해") {
                verify(exactly = 1) { travelRepository.findLeaderId(travel.id) }
                verify(exactly = 1) { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) }
                verify(exactly = 1) { travelPlaceRepository.decrementAllSequence(travelPlace.sequence) }
                verify(exactly = 1) { eventListener.publishEvent(any<TravelEvent>()) }
            }
            Then("여행 장소의 deleteAt이 null이 아니여야 하며") {
                travelPlace.deleteAt shouldNotBe null
            }
            Then("반환 값이 없어야 한다.") {
                result shouldBe Unit
            }
        }
    }

    Given("여행 주도자가 아닌 사용자로부터 삭제하는 여행 장소 데이터가 주어졌을 때") {
        val userId = 1L
        val travelPlaceId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { Random.nextLong() }
        }

        every { travelRepository.findLeaderId(travel.id) } returns travel.leaderId

        When("여행 장소 삭제를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelLeaderRequiredException> {
                    sut.softDelete(userId, travel.id, travelPlaceId)
                }
            }
        }
    }

    Given("DB에 존재하지 않은 여행 장소에 대해 삭제하는 데이터가 주어졌을 때") {
        val userId = 1L
        val travelPlaceId = 1L
        val travel = KotlinFixture.fixture<Travel> {
            property(Travel::leaderId) { userId }
        }

        every { travelRepository.findLeaderId(travel.id) } returns travel.leaderId
        every { travelPlaceRepository.findNotDeletedTravelPlace(travelPlaceId) } returns null

        When("여행 장소 삭제를 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelPlaceIdNotFoundException> {
                    sut.softDelete(userId, travel.id, travelPlaceId)
                }
            }
        }
    }

    Given("이전에 추가된 여행 장소의 순서를 변경하는 데이터가 주어졌을 때") {
        val travelId = 1L
        val travelDay = 3
        val location = "upper"
        val placeTotalCount = 5L
        val travelPlace = KotlinFixture.fixture<TravelPlace> {
            property(TravelPlace::sequence) { 3L }
            property(TravelPlace::travelDay) { travelDay }
        }

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns true
        every { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) } returns travelPlace
        every { travelPlaceRepository.countPlaceTotalNumber(travelId, travelDay) } returns placeTotalCount
        justRun { travelPlaceRepository.incrementSequence(more(0L)) }

        When("여행 장소 순서 변경을 요청하면") {
            val result = sut.updateTravelPlaceSequence(travelId, travelPlace.id, travelDay, location)

            Then("변경하고자 하는 데이터가 올바른지를 검증 후 순서를 변경하고") {
                verify(exactly = 1) { travelRepository.existsNotDeletedPlannedTravel(travelId) }
                verify(exactly = 1) { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) }
                verify(exactly = 1) { travelPlaceRepository.countPlaceTotalNumber(travelId, travelDay) }
                verify(exactly = 1) { travelPlaceRepository.incrementSequence(more(0L)) }
            }
            Then("변경한 데이터가 맞는지 확인 후") {
                travelPlace.sequence shouldBe 2L
            }
            Then("여행 PK와 날짜를 반환해야 한다.") {
                result?.travelId shouldBe travelId
                result?.travelDay shouldBe travelDay
            }
        }
    }

    Given("올바르지 않은 데이터(예: 첫 번째 여행 장소를 위로 올리려고 하는 경우)로 이전에 추가된 여행 장소의 순서를 변경하는 데이터가 주어졌을 때") {
        val travelId = 1L
        val travelDay = 2
        val placeTotalCount = 5L
        val location = "upper"
        val travelPlace = KotlinFixture.fixture<TravelPlace> {
            property(TravelPlace::sequence) { 1L }
        }

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns true
        every { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) } returns travelPlace
        every { travelPlaceRepository.countPlaceTotalNumber(travelId, travelDay) } returns placeTotalCount

        When("여행 장소 순서 변경을 요청하면") {
            val result = sut.updateTravelPlaceSequence(travelId, travelPlace.id, travelDay, location)

            Then("변경하고자 하는 데이터가 올바른지를 검증 후") {
                verify(exactly = 1) { travelRepository.existsNotDeletedPlannedTravel(travelId) }
                verify(exactly = 1) { travelPlaceRepository.findNotDeletedTravelPlace(travelPlace.id) }
                verify(exactly = 1) { travelPlaceRepository.countPlaceTotalNumber(travelId, travelDay) }
            }
            Then("null을 반환해야 한다.") {
                result shouldBe null
            }
        }
    }

    Given("DB에 존재하지 않은 여행에 속하는 장소의 순서를 변경하는 데이터가 주어졌을 때") {
        val travelId = 1L
        val travelPlaceId = 1L
        val travelDay = 3
        val location = "upper"

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns false

        When("여행 장소 순서 변경을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelIdNotFoundException> {
                    sut.updateTravelPlaceSequence(travelId, travelPlaceId, travelDay, location)
                }
            }
        }
    }

    Given("DB에 존재하지 않은 여행 장소의 순서를 변경하는 데이터가 주어졌을 때") {
        val travelId = 1L
        val travelPlaceId = 1L
        val travelDay = 3
        val location = "upper"

        every { travelRepository.existsNotDeletedPlannedTravel(travelId) } returns true
        every { travelPlaceRepository.findNotDeletedTravelPlace(travelPlaceId) } returns null

        When("여행 장소 순서 변경을 요청하면") {
            Then("에러가 발생해야 한다.") {
                shouldThrow<TravelPlaceIdNotFoundException> {
                    sut.updateTravelPlaceSequence(travelId, travelPlaceId, travelDay, location)
                }
            }
        }
    }
})
