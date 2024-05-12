package com.susuhan.travelpick.domain.travelmate.dto.response

import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import io.swagger.v3.oas.annotations.media.Schema

data class ParticipantMateListResponse(
    @Schema(description = "여행 메이트의 PK")
    val id: Long?,

    @Schema(description = "여행 메이트의 닉네임")
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