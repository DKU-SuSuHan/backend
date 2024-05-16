package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.dto.response.ParticipantMateInfoResponse
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

    fun getParticipantMateList(userId: Long, travelId: Long): List<ParticipantMateInfoResponse> {
        val leaderId = travelRepository.findLeaderId(travelId)

        TravelPolicy.isTravelLeader(userId, leaderId)

        return travelMateRepository.findAllParticipantMate(travelId)
            .map { travelMate ->  ParticipantMateInfoResponse.from(travelMate)}
    }
}