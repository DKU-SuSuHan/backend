package com.susuhan.travelpick.domain.travelplace.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class TravelPlaceSequenceUpdateResponse(
    @Schema(description = "여행지의 PK")
    val travelId: Long,

    @Schema(description = "여행 장소의 여행 날짜")
    val travelDay: Int,
) {

    companion object {
        fun of(travelId: Long, travelDay: Int) = TravelPlaceSequenceUpdateResponse(travelId, travelDay)
    }
}
