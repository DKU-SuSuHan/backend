package com.susuhan.travelpick.domain.travelplace.dto.response

import com.susuhan.travelpick.domain.travelplace.dto.TravelPlaceDto
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class ConfirmTravelPlaceListResponse(
    @Schema(description = "여행지의 PK")
    val travelId: Long,

    @Schema(description = "여행 장소의 여행 날짜")
    val travelDate: LocalDate,

    @Schema(description = "여행 장소 목록")
    val travelPlaceList: List<TravelPlaceDto>,

    @Schema(description = "여행 장소 목록의 하루 총 예산")
    val oneDayBudget: Long
) {

    companion object {
        fun from(
            travelId: Long,
            travelDate: LocalDate,
            travelPlaceList: List<TravelPlaceDto>,
            oneDayBudget: Long
        ) = ConfirmTravelPlaceListResponse(
            travelId,
            travelDate,
            travelPlaceList,
            oneDayBudget
        )
    }
}