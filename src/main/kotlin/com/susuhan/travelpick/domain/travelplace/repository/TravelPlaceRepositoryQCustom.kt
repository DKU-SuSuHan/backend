package com.susuhan.travelpick.domain.travelplace.repository

import com.susuhan.travelpick.domain.travelplace.dto.AddressInfo
import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace

interface TravelPlaceRepositoryQCustom {

    fun countPlaceTotalNumber(travelId: Long, travelDay: Int): Long

    fun findNotDeletedTravelPlace(id: Long): TravelPlace?

    fun findConfirmPlaceListForDay(travelId: Long, travelDay: Int): List<TravelPlace>

    fun findOneDayBudget(travelId: Long, travelDay: Int): Long

    fun findTotalBudget(travelId: Long): Long

    fun findPostcodeAndAddress(travelId: Long): List<AddressInfo>
}