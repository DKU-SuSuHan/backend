package com.susuhan.travelpick.domain.travel.dto.response

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.global.common.util.DateUtils

data class MyTravelResponse(
    val travelId: Long?,
    val title: String,
    val startAt: String,
    val endAt: String
    // TODO: 총 예산 필드 추가
) {

    companion object {
        fun from(travel: Travel): MyTravelResponse {
            return MyTravelResponse(
                travel?.id,
                travel.title,
                DateUtils.parse(travel.startAt),
                DateUtils.parse(travel.endAt)
            )
        }
    }
}
