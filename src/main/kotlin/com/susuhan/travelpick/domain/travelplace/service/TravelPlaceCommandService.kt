package com.susuhan.travelpick.domain.travelplace.service

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceCreateRequest
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceUpdateRequest
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceCreateResponse
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceUpdateResponse
import com.susuhan.travelpick.domain.travelplace.exception.TravelDateNotValidException
import com.susuhan.travelpick.domain.travelplace.exception.TravelPlaceIdNotFoundException
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
import com.susuhan.travelpick.global.common.policy.TravelPolicy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.Period

@Service
class TravelPlaceCommandService(
    private val travelPlaceRepository: TravelPlaceRepository,
    private val travelRepository: TravelRepository,
    private val travelMateRepository: TravelMateRepository
) {

    @Transactional
    fun createByHand(
        userId: Long, travelId: Long, request: TravelPlaceCreateRequest
    ): TravelPlaceCreateResponse {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        checkUserIsTravelLeader(userId, travelId)

        val travelDay = calculateTravelDay(travel, request.travelDate)

        val placeTotalNum = travelPlaceRepository.countPlaceTotalNumber(travelId, travelDay)

        travelPlaceRepository.save(
            request.toEntity(travel, travelDay, placeTotalNum + 1)
        )

        return TravelPlaceCreateResponse.of(travelId)
    }

    @Transactional
    fun update(
        userId: Long, travelId: Long, travelPlaceId: Long, request: TravelPlaceUpdateRequest
    ): TravelPlaceUpdateResponse {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        checkUserIsTravelLeader(userId, travelId)

        val travelPlace = (travelPlaceRepository.findNotDeletedTravelPlace(travelPlaceId)
            ?: throw TravelPlaceIdNotFoundException())

        travelPlace.updateTravelDay(calculateTravelDay(travel, request.travelDate))
        travelPlace.updateName(request.name)
        travelPlace.updatePostcode(request.postcode)
        travelPlace.updateAddress(request.address)
        travelPlace.updateBudget(request.budget)
        request.urlLink?.let { urlLink -> travelPlace.updateUrlLink(urlLink) }

        return TravelPlaceUpdateResponse.of(travelId, travelPlace.travelDay)
    }

    @Transactional
    fun softDelete(userId: Long, travelId: Long, travelPlaceId: Long) {
        if (!travelRepository.existNotDeletedPlannedTravel(travelId)) {
            throw TravelIdNotFoundException()
        }

        checkUserIsTravelLeader(userId, travelId)

        val travelPlace = (travelPlaceRepository.findNotDeletedTravelPlace(travelPlaceId)
            ?: throw TravelPlaceIdNotFoundException())

        travelPlace.softDelete()
    }

    private fun checkUserIsTravelLeader(userId: Long, travelId: Long) {
        val groupRole = travelMateRepository.findGroupRole(userId, travelId)
            ?: throw TravelMateIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, groupRole)
    }

    /**
     * 여행 시작 날짜와 특정 여행 장소를 방문할 날짜의 차이를 구해 D-day 계산
     */
    private fun calculateTravelDay(travel: Travel, travelDate: LocalDate): Int {
        if (travelDate !in travel.startAt..travel.endAt) {
            throw TravelDateNotValidException()
        }
        return Period.between(travel.startAt, travelDate).days + 1
    }
}