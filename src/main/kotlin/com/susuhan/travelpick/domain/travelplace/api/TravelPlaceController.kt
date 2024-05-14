package com.susuhan.travelpick.domain.travelplace.api

import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceCreateRequest
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceCreateResponse
import com.susuhan.travelpick.domain.travelplace.service.TravelPlaceCommandService
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
@Tag(name = "여행 장소 관련 API")
class TravelPlaceController(
    private val travelPlaceCommandService: TravelPlaceCommandService
) {

    @Operation(
        summary = "여행지 장소 수기 추가",
        description = """
            진행 중인 여행지에 대해 확정된 장소를 수기로 작성 받아 추가합니다.
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @PostMapping("/{travelId}/places")
    fun createTravelPlace(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @Valid @RequestBody travelPlaceCreateRequest: TravelPlaceCreateRequest
    ): ResponseEntity<TravelPlaceCreateResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(travelPlaceCommandService.createTravelPlace(
                customUserDetails.getUserId(), travelId, travelPlaceCreateRequest
            ))
    }
}