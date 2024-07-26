package com.susuhan.travelpick.global.event

import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace

data class TravelEvent(
    val action: TravelAction,
    val userId: Long,
    val travelId: Long,
    val travelPlace: TravelPlace?,
    val travelMate: TravelMate?,
) {

    constructor(
        action: TravelAction,
        userId: Long,
        travelId: Long,
        travelPlace: TravelPlace,
    ) : this(
        action = action,
        userId = userId,
        travelId = travelId,
        travelPlace = travelPlace,
        travelMate = null,
    )

    constructor(
        action: TravelAction,
        userId: Long,
        travelId: Long,
        travelMate: TravelMate,
    ) : this(
        action = action,
        userId = userId,
        travelId = travelId,
        travelPlace = null,
        travelMate = travelMate,
    )
}
