package com.susuhan.travelpick.domain.travelplace.repository

interface TravelPlaceRepositoryQCustom {

    fun countPlaceTotalNum(travelId: Long, travelDay: Int): Long
}