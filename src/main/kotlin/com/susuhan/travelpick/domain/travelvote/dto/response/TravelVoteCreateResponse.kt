package com.susuhan.travelpick.domain.travelvote.dto.response


import com.susuhan.travelpick.domain.travel.entity.Category
import com.susuhan.travelpick.domain.travelvote.entity.TravelVote
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class TravelVoteCreateResponse(
    @Schema(description = "투표 PK")
    val id: Long?,

    @Schema(description = "투표가 속한 카테고리 PK")
    val categoryId: Long?,

    @Schema(description="투표 제목")
    val title: String,

    @Schema(description="투표 종료 날짜")
    var expiredAt: LocalDate,

    @Schema(description="단일 투표 여부")
    var isSingle: Boolean,
) {

    companion object {
        fun from(travelVote: TravelVote, category: Category): TravelVoteCreateResponse {
            return TravelVoteCreateResponse(
                travelVote.id!!,
                category.id!!,
                travelVote.title,
                travelVote.expiredAt,
                travelVote.isSingle,
            )
        }
    }
}