package com.susuhan.travelpick.domain.travel.dto.response

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.entity.constant.Theme
import com.susuhan.travelpick.global.common.util.DateUtils
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Period

data class MyTravelResponse(
    @Schema(description = "여행지의 PK")
    val id: Long,

    @Schema(description = "여행지의 템플릿 이미지 번호")
    val templateNum: Long,

    @Schema(description = "여행지의 이름")
    val title: String,

    @Schema(description = "여행지의 테마")
    val theme: Theme,

    @Schema(description = "여행 시작 날짜")
    val startAt: String,

    @Schema(description = "여행 종료 날짜")
    val endAt: String,

    @Schema(description = "여행 시작과 종료 날짜의 차이")
    val dateDiff: Int,
) {

    companion object {
        fun from(travel: Travel) = MyTravelResponse(
            travel.id!!,
            travel.templateNum,
            travel.title,
            travel.theme,
            DateUtils.parse(travel.startAt),
            DateUtils.parse(travel.endAt),
            Period.between(travel.startAt, travel.endAt).days,
        )
    }
}
