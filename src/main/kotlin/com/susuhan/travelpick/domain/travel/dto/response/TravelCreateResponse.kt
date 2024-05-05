package com.susuhan.travelpick.domain.travel.dto.response

import com.susuhan.travelpick.domain.travel.entity.Travel
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class TravelCreateResponse(
    @Schema(description = "여행지의 이름")
    val title: String,

    @Schema(description = "행정 구역의 시, 도")
    val sido: String,

    @Schema(description = "행정 구역의 시, 군, 구")
    val sgg: String,

    @Schema(description = "여행 시작 날짜")
    val startAt: LocalDate,

    @Schema(description = "여행 종료 날짜")
    val endAt: LocalDate,
) {

    companion object {
        fun from(travel: Travel): TravelCreateResponse {
            return TravelCreateResponse(
                travel.title,
                travel.address.sido,
                travel.address.sgg,
                travel.startAt,
                travel.endAt
            )
        }
    }
}