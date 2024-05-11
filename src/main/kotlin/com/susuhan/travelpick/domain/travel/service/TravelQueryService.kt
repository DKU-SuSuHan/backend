package com.susuhan.travelpick.domain.travel.service

import com.susuhan.travelpick.domain.travel.dto.response.MyTravelListResponse
import com.susuhan.travelpick.domain.travel.dto.response.MyTravelResponse
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class TravelQueryService (
    private val travelMateRepository: TravelMateRepository
) {

    fun getTravel(userId: Long, travelId: Long): MyTravelResponse {
        val travel = travelMateRepository.findTravel(userId, travelId) ?: throw TravelIdNotFoundException()
        return MyTravelResponse.from(travel)
    }

    fun getPlannedTravelList(userId: Long): List<MyTravelListResponse> {
        return travelMateRepository.findPlannedTravel(userId)
            .map { travel -> MyTravelListResponse.from(travel) }
            .toList()
    }

    fun getEndedTravelList(userId: Long): List<MyTravelListResponse> {
        return travelMateRepository.findEndedTravel(userId)
            .map { travel -> MyTravelListResponse.from(travel) }
            .toList()
    }
}