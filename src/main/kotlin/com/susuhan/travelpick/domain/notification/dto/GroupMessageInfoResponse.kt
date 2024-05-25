package com.susuhan.travelpick.domain.notification.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.*
import com.susuhan.travelpick.domain.notification.entity.Notification
import com.susuhan.travelpick.domain.travelplace.dto.TravelPlaceMessageInfo
import com.susuhan.travelpick.domain.user.dto.UserProfileInfo
import java.time.LocalDateTime

@JsonInclude(Include.NON_NULL)
data class GroupMessageInfoResponse(
    val id: Long,
    val createAt: LocalDateTime,
    val message: String,
    val sendUserProfileInfo: UserProfileInfo,
    val travelPlaceMessageInfo: TravelPlaceMessageInfo?,
    val travelMateMessageInfo: UserProfileInfo?,
    val isMine: Boolean
) {

    companion object {
        fun from(notification: Notification) = GroupMessageInfoResponse(
            notification.id!!,
            notification.createAt,
            "${notification.sendUser.nickname}${notification.travelAction.message}",
            UserProfileInfo(
                notification.sendUser.nickname,
                notification.sendUser.profileImageUrl
            ),
            notification.travelPlace?.let {
                TravelPlaceMessageInfo(
                    it.travel.startAt.plusDays((it.travelDay - 1).toLong()),
                    it.travelDay,
                    it.name
                )
            },
            notification.travelMate?.let {
                UserProfileInfo(
                    it.user.nickname,
                    it.user.profileImageUrl
                )
            },
            notification.receiveUserId == notification.sendUser.id
        )
    }
}