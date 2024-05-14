package com.susuhan.travelpick.domain.travelplace.repository

import com.susuhan.travelpick.domain.travelplace.entity.TravelPlace

interface TravelPlaceRepositoryQCustom {

    fun countPlaceTotalNumber(travelId: Long, travelDay: Int): Long

    fun findConfirmPlaceListForDay(travelId: Long, travelDay: Int): List<TravelPlace>

    fun findOneDayBudget(travelId: Long, travelDay: Int): Long
}