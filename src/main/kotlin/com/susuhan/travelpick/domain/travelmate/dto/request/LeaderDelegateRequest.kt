package com.susuhan.travelpick.domain.travelmate.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class LeaderDelegateRequest(
    @Schema(description = "Leader 권한을 가질 여행 메이트의 PK")
    val travelMateId: Long,
)
