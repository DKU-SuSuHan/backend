package com.susuhan.travelpick.domain.travel.dto.response

import com.susuhan.travelpick.domain.travel.entity.Travel
import java.time.LocalDate
import java.time.Period

data class MyTravelListResponse(
    val id: Long?,
    val title: String,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val dateDiff: Int,
) {

    companion object {
        fun from(travel: Travel): MyTravelListResponse {
            return MyTravelListResponse(
                travel?.id,
                travel.title,
                travel.startAt,
                travel.endAt,
                Period.between(travel.startAt, travel.endAt).days
            )
        }
    }
}
