package com.susuhan.travelpick.domain.travelmate.exception

import com.susuhan.travelpick.global.exception.BusinessException
import com.susuhan.travelpick.global.exception.ErrorCode

class TravelLeaderDeletionException(travelMateId: Long)
    : BusinessException(ErrorCode.TRAVEL_LEADER_DELETION, "travelMateId = $travelMateId") {

    override fun isNecessaryToLog(): Boolean = false
}