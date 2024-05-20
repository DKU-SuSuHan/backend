package com.susuhan.travelpick.domain.travelplace.dto

import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import io.swagger.v3.oas.annotations.media.Schema

data class TravelPlaceDto(
    @Schema(description = "여행 장소의 PK")
    val id: Long,

    @Schema(description = "여행 장소의 여행 날짜")
    val travelDay: Int,

    @Schema(description = "여행 장소의 이름")
    val name: String,

    @Schema(description = "여행 장소의 우편 번호")
    val postcode: String,

    @Schema(description = "여행 장소의 도로명 주소")
    val address: String,

    @Schema(description = "여행 장소의 예산")
    val budget: Long,

    @Schema(description = "여행 장소의 순서")
    val sequence: Long,

    @Schema(description = "여행 장소의 참고 링크")
    val urlLink: String?
) {

    companion object {
        fun from(travelPlace: TravelPlace) = TravelPlaceDto(
            travelPlace.id!!,
            travelPlace.travelDay,
            travelPlace.name,
            travelPlace.postcode,
            travelPlace.address,
            travelPlace.budget,
            travelPlace.sequence,
            travelPlace.urlLink
        )
    }
}