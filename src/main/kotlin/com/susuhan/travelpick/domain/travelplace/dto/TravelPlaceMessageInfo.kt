package com.susuhan.travelpick.domain.travelplace.dto

import java.time.LocalDate

data class TravelPlaceMessageInfo(
    val travelDate: LocalDate,
    val travelDay: Int,
    val name: String,
    // TODO: 추가된 경로에 대한 필드 추가
    // TODO: 카테고리 필드 추가
)
