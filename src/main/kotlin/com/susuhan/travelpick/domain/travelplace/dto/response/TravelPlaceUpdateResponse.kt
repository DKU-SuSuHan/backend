package com.susuhan.travelpick.domain.travelplace.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class TravelPlaceUpdateResponse(
    @Schema(description = "수정한 여행 장소지를 가진 여행지의 PK")
    val travelId: Long,

    @Schema(description = "수정한 여행 장소의 여행 날짜 PK")
    val travelDay: Int,
) {

    companion object {
        fun of(travelId: Long, travelDay: Int) = TravelPlaceUpdateResponse(travelId, travelDay)
    }
}