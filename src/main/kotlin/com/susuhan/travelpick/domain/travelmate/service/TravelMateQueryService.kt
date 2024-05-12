package com.susuhan.travelpick.domain.travelmate.service

import com.susuhan.travelpick.domain.travel.exception.TravelIdNotFoundException
import com.susuhan.travelpick.domain.travel.repository.TravelRepository
import com.susuhan.travelpick.domain.travelmate.dto.response.ParticipantMateListResponse
import com.susuhan.travelpick.domain.travelmate.exception.TravelMateIdNotFoundException
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

    fun getParticipantMateList(userId: Long, travelId: Long): List<ParticipantMateListResponse> {
        if (!travelRepository.existsByIdAndDeleteAtIsNull(travelId)) {
            throw TravelIdNotFoundException()
        }

        checkUserIsTravelLeader(userId, travelId)

        return travelMateRepository.findAllParticipantMate(travelId)
            .map { travelMate ->  ParticipantMateListResponse.from(travelMate)}
            .toList()
    }

    private fun checkUserIsTravelLeader(userId: Long, travelId: Long) {
        val groupRole = travelMateRepository.findGroupRole(userId, travelId)
            ?: throw TravelMateIdNotFoundException()

        TravelPolicy.isTravelLeader(userId, groupRole)
    }
}