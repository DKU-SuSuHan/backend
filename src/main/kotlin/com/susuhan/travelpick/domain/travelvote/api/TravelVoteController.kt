package com.susuhan.travelpick.domain.travelvote.api

import com.susuhan.travelpick.domain.travelvote.dto.request.TravelVoteCreateRequest
import com.susuhan.travelpick.domain.travelvote.dto.response.TravelVoteCreateResponse
import com.susuhan.travelpick.domain.travelvote.service.TravelVoteCommandService
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
@Tag(name = "투표 관련 API")
class TravelVoteController(
    private val travelVoteCommandService: TravelVoteCommandService
){

    @Operation(
        summary = "새로운 투표 생성",
        description = "생성할 투표에 대한 정보를 받아 새 투표를 생성합니다.",
        security = [SecurityRequirement(name = "access-token")]
    )
    @PostMapping("/{travelId}/votes")
    fun createVote(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @Valid @RequestBody travelVoteCreateRequest: TravelVoteCreateRequest,
    ):ResponseEntity<TravelVoteCreateResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(travelVoteCommandService.createVote(
                customUserDetails.getUserId(), travelId, travelVoteCreateRequest
            ))
    }
}