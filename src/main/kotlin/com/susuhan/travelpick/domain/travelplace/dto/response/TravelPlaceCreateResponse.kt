package com.susuhan.travelpick.domain.travelplace.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class TravelPlaceCreateResponse(
    @Schema(description = "여행지의 PK")
    val travelId: Long,
) {

    companion object {
        fun of(travelId: Long) = TravelPlaceCreateResponse(travelId)
    }
}
