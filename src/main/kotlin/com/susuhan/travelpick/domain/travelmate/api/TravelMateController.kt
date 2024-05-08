package com.susuhan.travelpick.domain.travelmate.api

import com.susuhan.travelpick.domain.travelmate.dto.request.TravelMateCreateRequest
import com.susuhan.travelpick.domain.travelmate.dto.response.TravelMateCreateResponse
import com.susuhan.travelpick.domain.travelmate.service.TravelMateCommandService
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
    private val travelMateCommandService: TravelMateCommandService
) {

    @Operation(
        summary = "여행 메이트 추가",
        description = "여행지에 새로운 여행 메이트를 추가합니다.",
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
            .body(travelMateCommandService.createTravelMate(
                customUserDetails.userId, travelId, travelMateCreateRequest)
            )
    }
}