package com.susuhan.travelpick.domain.travelplace.service

import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceCreateRequest
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceCreateResponse
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
import com.susuhan.travelpick.global.common.policy.TravelPolicy
import org.springframework.stereotype.Service

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

        val placeNum = travelPlaceRepository.countPlaceTotalNum(travelId, request.travelDay)

        travelPlaceRepository.save(request.toEntity(travel, placeNum + 1))

        return TravelPlaceCreateResponse.of(travelId)
    }

    private fun checkUserIsTravelLeader(userId: Long, travelId: Long) {
        val groupRole = travelMateRepository.findGroupRole(userId, travelId)
            ?: throw TravelMateIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, groupRole)
    }
}