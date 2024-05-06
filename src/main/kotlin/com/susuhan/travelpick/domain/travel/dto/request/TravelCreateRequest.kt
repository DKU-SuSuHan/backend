package com.susuhan.travelpick.domain.travel.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class TravelCreateRequest(
    @Schema(description = "여행지의 템플릿 이미지 번호")
    val templateNum: Long,

    @Schema(description = "여행지의 이름")
    @field:NotBlank
    val title: String,

    @Schema(description = "행정 구역의 시, 도")
    @field:NotBlank
    val sido: String,

    @Schema(description = "행정 구역의 시, 군, 구")
    @field:NotBlank
    val sgg: String,

    @Schema(description = "여행 시작 날짜")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val startAt: LocalDate,

    @Schema(description = "여행 종료 날짜")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val endAt: LocalDate,
)
