package com.susuhan.travelpick.domain.travel.dto.response

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.global.common.util.DateUtils
import java.time.Period

data class MyTravelListResponse(
    val id: Long?,
    val title: String,
    val startAt: String,
    val endAt: String,
    val dateDiff: Int,
) {

    companion object {
        fun from(travel: Travel): MyTravelListResponse {
            return MyTravelListResponse(
                travel?.id,
                travel.title,
                DateUtils.parse(travel.startAt),
                DateUtils.parse(travel.endAt),
                Period.between(travel.startAt, travel.endAt).days
            )
        }
    }
}
