package com.susuhan.travelpick.domain.travelplace.dto.request

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class TravelPlaceCreateRequest(
    @Schema(description = "여행지 장소의 몇 일째 날짜")
    val travelDay: Int,

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
    val urlLink: String?
) {

    fun toEntity(travel: Travel, sequence: Long) = TravelPlace(
        travel = travel,
        travelDay = this.travelDay,
        name = this.name,
        postcode = this.postcode,
        address = this.address,
        budget = this.budget,
        sequence = sequence,
        urlLink = this.urlLink
    )
}
