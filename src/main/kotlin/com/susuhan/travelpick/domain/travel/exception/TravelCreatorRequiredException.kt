package com.susuhan.travelpick.domain.travel.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelCreatorRequiredException(userId: String?)
    : BusinessException(ErrorCode.TRAVEL_CREATOR_REQUIRED, "Travel.createdBy = $userId") {

    override fun isNecessaryToLog(): Boolean = true
}