package com.susuhan.travelpick.domain.travelmate.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class TravelMateCreateRequest(
    @Schema(description = "여행 메이트로 추가할 회원의 PK")
    val userId: Long
)