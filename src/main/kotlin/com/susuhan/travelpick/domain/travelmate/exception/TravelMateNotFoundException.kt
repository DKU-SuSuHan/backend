package com.susuhan.travelpick.domain.travelmate.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelMateNotFoundException(userId: Long)
    : BusinessException(ErrorCode.TRAVEL_MATE_NOT_FOUND, "회원의 PK = $userId") {

    override fun isNecessaryToLog(): Boolean = true
}