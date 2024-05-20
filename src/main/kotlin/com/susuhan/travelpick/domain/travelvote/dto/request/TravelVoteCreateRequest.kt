package com.susuhan.travelpick.domain.travelvote.dto.request

import com.susuhan.travelpick.domain.travel.entity.QTravel.travel
import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.entity.constant.Category
import com.susuhan.travelpick.domain.travelvote.entity.TravelVote
import com.susuhan.travelpick.domain.user.entity.User
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class TravelVoteCreateRequest (

    @Schema(description="카테고리")
    @field:NotNull
    var category: Category,

    @Schema(description="투표 제목")
    @field:NotBlank
    val title: String,

    @Schema(description="투표 종료 날짜")
    @field:DateTimeFormat(pattern = "yyyy-MM-dd")
    var expiredAt: LocalDate,

    @Schema(description="단일 투표 여부")
    var isSingle: Boolean,
) {
    fun toEntity(user: User, travel: Travel) = TravelVote(
        user = user,
        travel = travel,
        category = this.category,
        title = this.title,
        expiredAt = this.expiredAt,
        isSingle = this.isSingle,
    )
}