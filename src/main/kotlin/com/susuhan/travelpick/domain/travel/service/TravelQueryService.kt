package com.susuhan.travelpick.domain.travel.service

import com.susuhan.travelpick.domain.travel.dto.response.MyTravelInfoResponse
import com.susuhan.travelpick.domain.travel.dto.response.MyTravelResponse
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelplace.repository.TravelPlaceRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class TravelQueryService(
    private val travelMateRepository: TravelMateRepository,
    private val travelPlaceRepository: TravelPlaceRepository,
) {

    fun getMyTravel(userId: Long, travelId: Long): MyTravelInfoResponse {
        val travel = travelMateRepository.findTravel(userId, travelId)
            ?: throw TravelIdNotFoundException()

        val travelMate = travelMateRepository.findNotDeletedMateByUser(travelId, userId)
            ?: throw TravelMateIdNotFoundException()

        return MyTravelInfoResponse.from(
            travel,
            travelMate,
            totalBudget = travelPlaceRepository.findTotalBudget(travelId),
        )
    }

    fun getPlannedTravelList(userId: Long): List<MyTravelResponse> {
        return travelMateRepository.findPlannedTravel(userId)
            .map { travel -> MyTravelResponse.from(travel) }
    }

    fun getEndedTravelList(userId: Long): List<MyTravelResponse> {
        return travelMateRepository.findEndedTravel(userId)
            .map { travel -> MyTravelResponse.from(travel) }
    }
}
