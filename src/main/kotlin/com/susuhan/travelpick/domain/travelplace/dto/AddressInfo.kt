package com.susuhan.travelpick.domain.travelplace.dto

import io.swagger.v3.oas.annotations.media.Schema

data class AddressInfo(
    @Schema(description = "여행 장소의 PK")
    val id: Long,

    @Schema(description = "여행 장소의 우편 주소")
    val postcode: String,

    @Schema(description = "여행 장소의 도로명 주소")
    val address: String,
)
