package com.susuhan.travelpick.domain.travelmate.dto.response

import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import io.swagger.v3.oas.annotations.media.Schema

data class ParticipantMateInfoResponse(
    @Schema(description = "여행 메이트의 PK")
    val id: Long,

    @Schema(description = "여행 메이트의 닉네임")
    val nickname: String?
) {

    companion object {
        fun from(travelMate: TravelMate) = ParticipantMateInfoResponse(
            travelMate.id!!,
            travelMate.user.nickname
        )
    }
}