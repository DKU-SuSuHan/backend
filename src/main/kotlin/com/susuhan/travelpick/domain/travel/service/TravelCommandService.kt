package com.susuhan.travelpick.domain.travel.service

import com.susuhan.travelpick.domain.travel.dto.request.TravelCreateRequest
import com.susuhan.travelpick.domain.travel.dto.request.TravelUpdateRequest
import com.susuhan.travelpick.domain.travel.dto.response.TravelCreateResponse
import com.susuhan.travelpick.domain.travel.dto.response.TravelUpdateResponse
import com.susuhan.travelpick.domain.travel.entity.Address
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelCreatorRequiredException
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TravelCommandService(
    private val travelRepository: TravelRepository
) {

    @Transactional
    fun createTravel(request: TravelCreateRequest): TravelCreateResponse {
        val travel = Travel(
            templateNum = request.templateNum,
            address = Address(request.sido, request.sgg),
            title = request.title,
            startAt = request.startAt,
            endAt = request.endAt
        )
        val savedTravel = travelRepository.save(travel)
        return TravelCreateResponse.from(savedTravel)
    }

    @Transactional
    fun updateTravel(userId: String, travelId: Long, request: TravelUpdateRequest): TravelUpdateResponse {
        var travel = travelRepository.findById(travelId) ?: throw TravelIdNotFoundException()

        if (userId != travel.createdBy) {
            throw TravelCreatorRequiredException()
        }

        travel.updateTitle(request.title)
        travel.updateStartAt(request.startAt)
        travel.updateEndAt(request.endAt)
        travel.updateAddress(Address(request.sido, request.sgg))
        travel.updateTemplateNum(request.templateNum)

        return TravelUpdateResponse.from(travel)
    }
}