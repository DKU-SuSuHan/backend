package com.susuhan.travelpick.domain.travel.dto.response

import com.susuhan.travelpick.domain.travel.entity.Travel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
                parseDate(travel.startAt),
                parseDate(travel.endAt)
            )
        }

        fun parseDate(date: LocalDate): String {
            val pattern = if (date.year == LocalDate.now().year) {
                "M월 d일"
            } else {
                "yyyy년 M월 d일"
            }
            val formatter = DateTimeFormatter.ofPattern(pattern)
            return date.format(formatter)
        }
    }
}
