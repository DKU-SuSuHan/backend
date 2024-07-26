package com.susuhan.travelpick.domain.travelplace.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelDateNotValidException : BusinessException(ErrorCode.TRAVEL_DATE_NOT_VALID) {

    override fun isNecessaryToLog(): Boolean = true
}
