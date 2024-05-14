package com.susuhan.travelpick.domain.travelmate.dto.request

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema

data class TravelMateCreateRequest(
    @Schema(description = "여행 메이트로 추가할 회원의 PK")
    val userId: Long
) {

    fun toEntity(user: User, travel: Travel) = TravelMate(
        user = user,
        travel = travel,
        groupRole = GroupRole.PARTICIPANT
    )
}