package com.susuhan.travelpick.global.event

import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace

data class TravelEvent(
    val action: TravelAction,
    val userId: Long,
    val travelId: Long,
    val travelPlace: TravelPlace?
)