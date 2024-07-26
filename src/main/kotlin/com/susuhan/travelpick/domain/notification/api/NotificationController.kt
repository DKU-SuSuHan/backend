package com.susuhan.travelpick.domain.notification.api

import com.susuhan.travelpick.domain.notification.dto.GroupMessageInfoResponse
import com.susuhan.travelpick.domain.notification.service.NotificationCommandService
import com.susuhan.travelpick.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/notifications")
@RestController
@Tag(name = "여행 그룹 메시지 관련 API")
class NotificationController(
    private val notificationCommandService: NotificationCommandService,
) {

    @Operation(
        summary = "그룹 메시지 목록 조회",
        description = "여행지 그룹의 자동 생성 메시지 목록을 조회합니다.",
        security = [SecurityRequirement(name = "access-token")],
    )
    @PutMapping("/travels/{travelId}")
    fun getGroupMessageList(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable("travelId") travelId: Long,
    ): ResponseEntity<List<GroupMessageInfoResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                notificationCommandService.getGroupMessageList(
                    customUserDetails.getUserId(),
                    travelId,
                ),
            )
    }
}
