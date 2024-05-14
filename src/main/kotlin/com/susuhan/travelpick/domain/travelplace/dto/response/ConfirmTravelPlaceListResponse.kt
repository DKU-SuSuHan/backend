package com.susuhan.travelpick.domain.travelplace.dto.response

import com.susuhan.travelpick.domain.travelplace.dto.TravelPlaceDto

data class ConfirmTravelPlaceListResponse(
    val travelId: Long,
    val travelPlaceList: List<TravelPlaceDto>,
    val oneDayBudget: Long
) {

    companion object {
        fun from(
            travelId: Long, travelPlaceList: List<TravelPlaceDto>, oneDayBudget: Long
        ) = ConfirmTravelPlaceListResponse(
            travelId,
            travelPlaceList,
            oneDayBudget
        )
    }
}