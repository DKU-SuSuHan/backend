package com.susuhan.travelpick.domain.travel.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelLeaderRequiredException(userId: Long) :
    BusinessException(ErrorCode.TRAVEL_LEADER_REQUIRED, "회원의 PK = $userId") {

    override fun isNecessaryToLog(): Boolean = true
}
