package com.susuhan.travelpick.domain.travelmate.dto.response

import com.susuhan.travelpick.domain.travelmate.entity.TravelMate

data class ParticipantMateListResponse(
    val id: Long?,
    val nickname: String?
) {

    companion object {
        fun from(travelMate: TravelMate): ParticipantMateListResponse {
            return ParticipantMateListResponse(
                travelMate?.id,
                travelMate.user?.nickname
            )
        }
    }
}