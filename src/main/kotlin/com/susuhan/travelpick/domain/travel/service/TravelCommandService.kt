package com.susuhan.travelpick.domain.travel.service

import com.susuhan.travelpick.domain.travel.dto.request.TravelCreateRequest
import com.susuhan.travelpick.domain.travel.dto.request.TravelUpdateRequest
import com.susuhan.travelpick.domain.travel.dto.response.TravelCreateResponse
import com.susuhan.travelpick.domain.travel.dto.response.TravelUpdateResponse
import com.susuhan.travelpick.domain.travel.entity.Address
import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.domain.travelmate.service.TravelMateCommandService
import com.susuhan.travelpick.global.common.policy.TravelPolicy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TravelCommandService(
    private val travelRepository: TravelRepository,
    private val travelMateRepository: TravelMateRepository,
    private val travelMateCommandService: TravelMateCommandService
) {

    @Transactional
    fun createTravel(userId: Long, request: TravelCreateRequest): TravelCreateResponse {
        val savedTravel = travelRepository.save(
            request.toEntity()
        )

        travelMateCommandService.createTravelLeader(userId, savedTravel)

        return TravelCreateResponse.from(savedTravel)
    }

    @Transactional
    fun updateTravel(
        userId: Long, travelId: Long, request: TravelUpdateRequest
    ): TravelUpdateResponse {
        val travel = travelRepository.findByIdAndDeleteAtIsNull(travelId)
            ?: throw TravelIdNotFoundException()

        checkUserIsTravelLeader(userId, travelId)

        travel.updateTitle(request.title)
        travel.updateStartAt(request.startAt)
        travel.updateEndAt(request.endAt)
        travel.updateAddress(Address(request.sido, request.sgg))
        travel.updateTemplateNum(request.templateNum)

        return TravelUpdateResponse.from(travel)
    }

    @Transactional
    fun deleteTravel(userId: Long, travelId: Long) {
        val travel = travelRepository.findByIdAndDeleteAtIsNull(travelId)
            ?: throw TravelIdNotFoundException()

        checkUserIsTravelLeader(userId, travelId)

        // 여행지 삭제 전, 여행 메이트 전체 삭제
        travelMateCommandService.deleteAll(travelId)

        travel.softDelete()
    }

    private fun checkUserIsTravelLeader(userId: Long, travelId: Long) {
        val groupRole = travelMateRepository.findGroupRoleByUserIdAndTravelId(userId, travelId)
            ?: throw TravelMateIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, groupRole)
    }
}