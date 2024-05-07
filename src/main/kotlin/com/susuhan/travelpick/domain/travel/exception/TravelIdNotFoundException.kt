package com.susuhan.travelpick.domain.travel.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelIdNotFoundException : BusinessException(ErrorCode.TRAVEL_ID_NOT_FOUND) {

    override fun isNecessaryToLog(): Boolean {
        return false
    }
}