package com.susuhan.travelpick.domain.travelplace.service

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceCreateRequest
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceUpdateRequest
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceCreateResponse
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceSequenceUpdateResponse
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceUpdateResponse
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import com.susuhan.travelpick.domain.travelplace.exception.TravelDateNotValidException
import com.susuhan.travelpick.domain.travelplace.exception.TravelPlaceIdNotFoundException
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
import com.susuhan.travelpick.global.common.policy.TravelPolicy
import com.susuhan.travelpick.global.event.TravelAction
import com.susuhan.travelpick.global.event.TravelEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.Period

@Service
class TravelPlaceCommandService(
    private val travelPlaceRepository: TravelPlaceRepository,
    private val travelRepository: TravelRepository,
    private val eventListener: ApplicationEventPublisher
) {

    @Transactional
    fun create(
        userId: Long, travelId: Long, request: TravelPlaceCreateRequest
    ): TravelPlaceCreateResponse {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, travel.leaderId)

        val travelDay = calculateTravelDay(travel, request.travelDate)

        val travelPlace = request.toEntity(
            travel,
            travelDay,
            sequence = travelPlaceRepository.countPlaceTotalNumber(travelId, travelDay) + 1
        )

        travelPlaceRepository.save(travelPlace)

        eventListener.publishEvent(TravelEvent(
            TravelAction.CREATE_PLACE, userId, travelId, travelPlace
        ))

        return TravelPlaceCreateResponse.of(travelId)
    }

    @Transactional
    fun update(
        userId: Long, travelId: Long, travelPlaceId: Long, request: TravelPlaceUpdateRequest
    ): TravelPlaceUpdateResponse {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, travel.leaderId)

        val travelPlace = (travelPlaceRepository.findNotDeletedTravelPlace(travelPlaceId)
            ?: throw TravelPlaceIdNotFoundException())

        updateTravelPlace(travelPlace, travel, request)

        return TravelPlaceUpdateResponse.of(travelId, travelPlace.travelDay)
    }

    @Transactional
    fun softDelete(userId: Long, travelId: Long, travelPlaceId: Long) {
        val leaderId = travelRepository.findLeaderId(travelId)

        TravelPolicy.isTravelLeader(userId, leaderId)

        val travelPlace = travelPlaceRepository.findNotDeletedTravelPlace(travelPlaceId)
            ?: throw TravelPlaceIdNotFoundException()

        travelPlace.softDelete()
        
        travelPlaceRepository.decrementAllSequence(travelPlace.sequence)

        eventListener.publishEvent(TravelEvent(
            TravelAction.DELETE_PLACE, userId, travelId, travelPlace
        ))
    }

    @Transactional
    fun updateTravelPlaceSequence(
        travelId: Long, travelPlaceId: Long, travelDay: Int, location: String
    ): TravelPlaceSequenceUpdateResponse? {
        if (!travelRepository.existsNotDeletedPlannedTravel(travelId)) {
            throw TravelIdNotFoundException()
        }

        val travelPlace = travelPlaceRepository.findNotDeletedTravelPlace(travelPlaceId)
            ?: throw TravelPlaceIdNotFoundException()

        val placeTotalNumber = travelPlaceRepository.countPlaceTotalNumber(travelId, travelDay)

        // 유효한 요청이 아니라고 판단된다면 null 반환
        if (!isValidSequenceRequest(travelPlace.sequence, placeTotalNumber, location)) {
            return null
        }

        updateSequence(location, travelPlace, placeTotalNumber)

        return TravelPlaceSequenceUpdateResponse.of(travelId, travelPlace.travelDay)
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

    private fun updateTravelPlace(
        travelPlace: TravelPlace, travel: Travel, request: TravelPlaceUpdateRequest
    ) {
        travelPlace.updateTravelDay(calculateTravelDay(travel, request.travelDate))
        travelPlace.updateName(request.name)
        travelPlace.updatePostcode(request.postcode)
        travelPlace.updateAddress(request.address)
        travelPlace.updateBudget(request.budget)
        request.urlLink?.let { urlLink -> travelPlace.updateUrlLink(urlLink) }
    }

    /**
     * 이미 최상단에 존재하는 여행지를 한 칸 위 또는 최상단으로 이동시키려고 하거나
     * 이미 최하단에 존재하는 여행지를 한 칸 아래 또는 최하단으로 이동시키려고 하는 요청라면 유효한 요청이 아니라고 판단
     */
    private fun isValidSequenceRequest(sequence: Long, placeTotalNumber: Long, location: String): Boolean {
        return !((sequence == 1L && (location == "upper" || location == "top")) ||
                (sequence == placeTotalNumber && (location == "lower" || location == "bottom")))
    }

    private fun updateSequence(
        location: String, travelPlace: TravelPlace, placeTotalNumber: Long
    ) {
        val sequence = travelPlace.sequence

        when (location) {
            "upper" -> {
                travelPlaceRepository.incrementSequence(sequence - 1)
                travelPlace.updateSequence(sequence - 1)
            }
            "lower" -> {
                travelPlaceRepository.decrementSequence(sequence + 1)
                travelPlace.updateSequence(sequence + 1)
            }
            "top" -> {
                travelPlaceRepository.incrementAllSequence(sequence)
                travelPlace.updateSequence(1)
            }
            "bottom" -> {
                travelPlaceRepository.decrementAllSequence(sequence)
                travelPlace.updateSequence(placeTotalNumber)
            }
        }
    }
}