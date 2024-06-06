package com.susuhan.travelpick.domain.travel.dto.response

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.global.common.util.DateUtils
import io.swagger.v3.oas.annotations.media.Schema
import java.time.temporal.ChronoUnit

data class MyTravelInfoResponse(
    @Schema(description = "여행지의 PK")
    val travelId: Long,

    @Schema(description = "여행을 조회한 사용자의 여행지 메이트 PK")
    val travelMateId: Long,

    @Schema(description = "여행지의 이름")
    val title: String,

    @Schema(description = "행정 구역의 시, 도")
    val sido: String,

    @Schema(description = "행정 구역의 시, 군, 구")
    val sgg: String,

    @Schema(description = "여행 시작 날짜")
    val startAt: String,

    @Schema(description = "여행 종료 날짜")
    val endAt: String,

    @Schema(description = "여행지의 권한")
    val groupRole: GroupRole,

    @Schema(description = "여행 총 날짜")
    val totalDay: Long,

    @Schema(description = "여행지의 총 예산")
    val totalBudget: Long
) {

    companion object {
        fun from(travel: Travel, travelMate: TravelMate, totalBudget: Long) = MyTravelInfoResponse(
            travel.id!!,
            travelMate.id!!,
            travel.title,
            travel.address.sido,
            travel.address.sgg,
            DateUtils.parse(travel.startAt),
            DateUtils.parse(travel.endAt),
            travelMate.groupRole,
            ChronoUnit.DAYS.between(travel.startAt, travel.endAt) + 1,
            totalBudget
        )
    }
}
