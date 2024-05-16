package com.susuhan.travelpick.domain.travel.service

import com.susuhan.travelpick.domain.travel.dto.request.TravelCreateRequest
import com.susuhan.travelpick.domain.travel.dto.request.TravelUpdateRequest
import com.susuhan.travelpick.domain.travel.dto.response.TravelCreateResponse
import com.susuhan.travelpick.domain.travel.dto.response.TravelUpdateResponse
import com.susuhan.travelpick.domain.travel.entity.Address
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
    fun create(userId: Long, request: TravelCreateRequest): TravelCreateResponse {
        val savedTravel = travelRepository.save(
            request.toEntity(userId)
        )

        travelMateCommandService.createTravelLeader(userId, savedTravel)

        return TravelCreateResponse.from(savedTravel)
    }

    @Transactional
    fun update(
        userId: Long, travelId: Long, request: TravelUpdateRequest
    ): TravelUpdateResponse {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, travel.leaderId)

        travel.updateTitle(request.title)
        travel.updateStartAt(request.startAt)
        travel.updateEndAt(request.endAt)
        travel.updateAddress(Address(request.sido, request.sgg))
        travel.updateTemplateNum(request.templateNum)

        return TravelUpdateResponse.from(travel)
    }

    @Transactional
    fun softDelete(userId: Long, travelId: Long) {
        val travel = travelRepository.findNotDeletedPlannedTravel(travelId)
            ?: throw TravelIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, travel.leaderId)

        // 여행지 삭제 전, 여행 메이트 전체 삭제
        travelMateCommandService.softDeleteAll(travelId)

        travel.softDelete()
    }
}