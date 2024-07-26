package com.susuhan.travelpick.domain.travelplace.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class TravelPlaceUpdateRequest(
    @Schema(description = "여행지 장소에 방문할 날짜")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    val travelDate: LocalDate,

    @Schema(description = "여행지 장소의 이름")
    @field:NotBlank
    val name: String,

    @Schema(description = "여행지 장소의 우편번호")
    @field:NotBlank
    val postcode: String,

    @Schema(description = "여행지 장소의 도로명 주소")
    @field:NotBlank
    val address: String,

    @Schema(description = "여행지 장소의 1인당 예산")
    val budget: Long,

    @Schema(description = "여행지 장소의 참고 링크")
    val urlLink: String?,
)
