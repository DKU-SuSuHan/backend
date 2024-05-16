package com.susuhan.travelpick.global.common.policy

import com.susuhan.travelpick.domain.travel.exception.TravelLeaderRequiredException
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.travelmate.exception.TravelLeaderDeletionException

class TravelPolicy {

    companion object {
        fun isTravelLeader(userId: Long, leaderId: Long) {
            if (userId != leaderId) {
                throw TravelLeaderRequiredException(userId)
            }
        }

        fun isTravelLeaderDeletion(travelMate: TravelMate) {
            if (travelMate.groupRole == GroupRole.LEADER) {
                throw TravelLeaderDeletionException(travelMate.id)
            }
        }
    }
}