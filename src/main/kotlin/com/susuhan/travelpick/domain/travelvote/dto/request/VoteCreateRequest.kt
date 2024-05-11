package com.susuhan.travelpick.domain.travelvote.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class VoteCreateRequest (
    @Schema(description="투표 제목")
    @field:NotBlank
    val title: String,

    @Schema(description="투표 종료 날짜")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    var expiredAt: LocalDate,

    @Schema(description="단일 투표 여부")
    var isSingle: Boolean,
)