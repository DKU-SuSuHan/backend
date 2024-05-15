package com.susuhan.travelpick.domain.travelplace.dto.response

import com.susuhan.travelpick.domain.travelplace.dto.TravelPlaceDto
import java.time.LocalDate

data class ConfirmTravelPlaceListResponse(
    val travelId: Long,
    val travelDate: LocalDate,
    val travelPlaceList: List<TravelPlaceDto>,
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