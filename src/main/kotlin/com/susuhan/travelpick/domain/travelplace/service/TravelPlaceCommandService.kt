package com.susuhan.travelpick.domain.travelplace.service

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceCreateRequest
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceCreateResponse
import com.susuhan.travelpick.domain.travelplace.exception.TravelDateNotValidException
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
import com.susuhan.travelpick.global.common.policy.TravelPolicy
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.Period

@Service
class TravelPlaceCommandService(
    private val travelPlaceRepository: TravelPlaceRepository,
    private val travelRepository: TravelRepository,
    private val travelMateRepository: TravelMateRepository
) {

    fun createTravelPlace(
        userId: Long, travelId: Long, request: TravelPlaceCreateRequest
    ): TravelPlaceCreateResponse {
        val travel = travelRepository.findByIdAndDeleteAtIsNull(travelId)
            ?: throw TravelIdNotFoundException()

        checkUserIsTravelLeader(userId, travelId)

        val travelDay = calculateTravelDay(travel, request.travelDate)

        val placeTotalNum = travelPlaceRepository.countPlaceTotalNum(travelId, travelDay)

        travelPlaceRepository.save(
            request.toEntity(travel, travelDay, placeTotalNum + 1)
        )

        return TravelPlaceCreateResponse.of(travelId)
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