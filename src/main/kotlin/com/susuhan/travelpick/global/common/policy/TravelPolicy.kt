package com.susuhan.travelpick.global.common.policy

import com.susuhan.travelpick.domain.travel.entity.Travel
import com.susuhan.travelpick.domain.travel.exception.TravelCreatorRequiredException
import com.susuhan.travelpick.domain.travelmate.entity.TravelMate
import com.susuhan.travelpick.domain.travelmate.entity.constant.GroupRole
import com.susuhan.travelpick.domain.travelmate.exception.TravelLeaderDeletionException

class TravelPolicy {

    companion object {
        fun isTravelCreator(userId: String, travel: Travel) {
            if (userId != travel.createdBy) {
                throw TravelCreatorRequiredException(travel.createdBy)
            }
        }

        fun isTravelLeaderDeletion(travelMate: TravelMate) {
            if (travelMate.groupRole == GroupRole.LEADER) {
                throw TravelLeaderDeletionException(travelMate.id)
            }
        }
    }
}