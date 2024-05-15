package com.susuhan.travelpick.domain.travelplace.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelPlaceIdNotFoundException : BusinessException(ErrorCode.TRAVEL_PLACE_ID_NOT_FOUND) {

    override fun isNecessaryToLog(): Boolean = false
}