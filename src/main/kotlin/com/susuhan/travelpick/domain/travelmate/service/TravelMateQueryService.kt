package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.dto.response.ParticipantMateListResponse
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.travelmate.repository.TravelMateRepository
import com.susuhan.travelpick.global.common.policy.TravelPolicy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class TravelMateQueryService(
    private val travelMateRepository: TravelMateRepository,
    private val travelRepository: TravelRepository
) {

    fun getParticipantMateList(userId: String, travelId: Long): List<ParticipantMateListResponse> {
        val travel = travelRepository.findByIdAndDeleteAtIsNull(travelId) ?: throw TravelIdNotFoundException()
        TravelPolicy.isTravelCreator(userId, travel)

        return travelMateRepository.findAllParticipantMateByTravelId(GroupRole.PARTICIPANT, travelId)
            .map { travelMate ->  ParticipantMateListResponse.from(travelMate)}
            .toList()
    }
}