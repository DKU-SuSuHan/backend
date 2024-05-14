package com.susuhan.travelpick.domain.travelplace.dto

import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace

data class TravelPlaceDto(
    val id: Long,
    val travelDay: Int,
    val name: String,
    val postcode: String,
    val address: String,
    val budget: Long,
    val sequence: Long,
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
            travelPlace?.urlLink
        )
    }
}