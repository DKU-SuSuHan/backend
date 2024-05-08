package com.susuhan.travelpick.domain.travelmate.dto.response

import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import io.swagger.v3.oas.annotations.media.Schema

data class TravelMateCreateResponse(
    @Schema(description = "추가한 여행 메이트의 PK")
    val travelMateId: Long
) {

    companion object {
        fun from(travelMate: TravelMate): TravelMateCreateResponse {
            return TravelMateCreateResponse(travelMate.id!!)
        }
    }
}