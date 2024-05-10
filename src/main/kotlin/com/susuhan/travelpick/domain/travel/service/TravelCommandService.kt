package com.susuhan.travelpick.domain.travel.service

import com.susuhan.travelpick.domain.travel.dto.request.TravelCreateRequest
import com.susuhan.travelpick.domain.travel.dto.request.TravelUpdateRequest
import com.susuhan.travelpick.domain.travel.dto.response.TravelCreateResponse
import com.susuhan.travelpick.domain.travel.dto.response.TravelUpdateResponse
import com.susuhan.travelpick.domain.travel.entity.Address
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.service.TravelMateCommandService
import com.susuhan.travelpick.global.common.policy.TravelPolicy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TravelCommandService(
    private val travelRepository: TravelRepository,
    private val travelMateCommandService: TravelMateCommandService
) {

    @Transactional
    fun createTravel(userId: Long, request: TravelCreateRequest): TravelCreateResponse {
        val travel = Travel(
            templateNum = request.templateNum,
            address = Address(request.sido, request.sgg),
            title = request.title,
            startAt = request.startAt,
            endAt = request.endAt
        )
        val savedTravel = travelRepository.save(travel)

        travelMateCommandService.createTravelLeader(userId, savedTravel)

        return TravelCreateResponse.from(savedTravel)
    }

    @Transactional
    fun updateTravel(userId: String, travelId: Long, request: TravelUpdateRequest): TravelUpdateResponse {
        var travel = travelRepository.findByIdAndDeleteAtIsNull(travelId) ?: throw TravelIdNotFoundException()
        TravelPolicy.isTravelCreator(userId, travel)

        travel.updateTitle(request.title)
        travel.updateStartAt(request.startAt)
        travel.updateEndAt(request.endAt)
        travel.updateAddress(Address(request.sido, request.sgg))
        travel.updateTemplateNum(request.templateNum)

        return TravelUpdateResponse.from(travel)
    }

    @Transactional
    fun deleteTravel(userId: String, travelId: Long) {
        var travel = travelRepository.findByIdAndDeleteAtIsNull(travelId) ?: throw TravelIdNotFoundException()
        TravelPolicy.isTravelCreator(userId, travel)

        // 여행지 삭제 전, 여행 메이트 전체 삭제
        travelMateCommandService.deleteAll(travelId)

        travel.softDelete()
    }
}