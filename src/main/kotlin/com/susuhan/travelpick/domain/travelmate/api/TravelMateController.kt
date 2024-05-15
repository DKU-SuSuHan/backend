package com.susuhan.travelpick.domain.travelmate.api

import com.susuhan.travelpick.domain.travelmate.dto.request.LeaderDelegateRequest
import com.susuhan.travelpick.domain.travelmate.dto.request.TravelMateCreateRequest
import com.susuhan.travelpick.domain.travelmate.dto.response.LeaderDelegateResponse
import com.susuhan.travelpick.domain.travelmate.dto.response.ParticipantMateInfoResponse
import com.susuhan.travelpick.domain.travelmate.dto.response.TravelMateCreateResponse
import com.susuhan.travelpick.domain.travelmate.service.TravelMateCommandService
import com.susuhan.travelpick.domain.travelmate.service.TravelMateQueryService
import com.susuhan.travelpick.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/v1/travels")
@RestController
@Tag(name = "여행 메이트 관련 API")
class TravelMateController(
    private val travelMateCommandService: TravelMateCommandService,
    private val travelMateQueryService: TravelMateQueryService
) {

    @Operation(
        summary = "여행 메이트 추가",
        description = """
            여행지에 새로운 여행 메이트를 추가합니다.
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @PostMapping("/{travelId}/mates")
    fun createTravelMate(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @Valid @RequestBody travelMateCreateRequest: TravelMateCreateRequest
    ): ResponseEntity<TravelMateCreateResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(travelMateCommandService.createTravelParticipant(
                customUserDetails.getUserId(), travelId, travelMateCreateRequest
            ))
    }

    @Operation(
        summary = "여행 메이트 삭제",
        description = """
            여행지의 기존 여행 메이트를 삭제합니다. 
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @DeleteMapping("/{travelId}/mates/{travelMateId}")
    fun softDeleteTravelMate(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @PathVariable(name = "travelMateId") travelMateId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(travelMateCommandService.softDeleteTravelMate(
                customUserDetails.getUserId(), travelId, travelMateId
            ))
    }

    @Operation(
        summary = "참여자 역할의 여행 메이트 목록 조회",
        description = """
            여행의 참여자 역할을 가진 여행 메이트 목록 조회합니다. 
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @GetMapping("/{travelId}/mates")
    fun getParticipantMateList(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
    ): ResponseEntity<List<ParticipantMateInfoResponse>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(travelMateQueryService.getParticipantMateList(
                customUserDetails.getUserId(), travelId
            ))
    }

    @Operation(
        summary = "여행의 Leader 권한 위임",
        description = """
            여행마다 한 명씩 존재하는 Leader 권한을 다른 여행 메이트에게 위임합니다. 
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @PutMapping("/{travelId}/mates/delegation/role")
    fun delegateLeaderRole(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @Valid @RequestBody leaderDelegateRequest: LeaderDelegateRequest
    ): ResponseEntity<LeaderDelegateResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(travelMateCommandService.delegateLeaderRole(
                customUserDetails.getUserId(), travelId, leaderDelegateRequest
            ))
    }
}