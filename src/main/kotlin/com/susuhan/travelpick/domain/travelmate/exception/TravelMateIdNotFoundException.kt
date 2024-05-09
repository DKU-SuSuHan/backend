package com.susuhan.travelpick.domain.travelmate.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelMateIdNotFoundException : BusinessException(ErrorCode.TRAVEL_MATE_ID_NOT_FOUND) {

    override fun isNecessaryToLog(): Boolean = false
}