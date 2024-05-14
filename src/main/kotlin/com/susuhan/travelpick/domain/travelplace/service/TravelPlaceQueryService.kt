package com.susuhan.travelpick.domain.travelplace.service

import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelplace.dto.TravelPlaceDto
import com.susuhan.travelpick.domain.travelplace.dto.response.ConfirmTravelPlaceListResponse
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class TravelPlaceQueryService(
    private val travelPlaceRepository: TravelPlaceRepository,
    private val travelRepository: TravelRepository,
    private val travelMateRepository: TravelMateRepository
) {

    fun getConfirmPlaceList(userId: Long, travelId: Long, travelDay: Int): ConfirmTravelPlaceListResponse {
        if (!travelRepository.existNotDeletedPlannedTravel(travelId)) {
            throw TravelIdNotFoundException()
        }

        if (!travelMateRepository.existNotDeletedMate(userId, travelId)) {
            throw TravelMateIdNotFoundException()
        }

        val travelPlaceList = travelPlaceRepository.findConfirmPlaceListForDay(travelId, travelDay)
            .map { travelPlace -> TravelPlaceDto.from(travelPlace) }
            .toList()

        val oneDayBudget = travelPlaceRepository.findOneDayBudget(travelId, travelDay)

        return ConfirmTravelPlaceListResponse.from(
            travelId, travelPlaceList, oneDayBudget
        )
    }
}