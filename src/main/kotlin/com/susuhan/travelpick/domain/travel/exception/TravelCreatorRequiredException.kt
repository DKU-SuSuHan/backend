package com.susuhan.travelpick.domain.travel.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelCreatorRequiredException : BusinessException(ErrorCode.TRAVEL_CREATOR_REQUIRED) {

    override fun isNecessaryToLog(): Boolean {
        return true
    }
}