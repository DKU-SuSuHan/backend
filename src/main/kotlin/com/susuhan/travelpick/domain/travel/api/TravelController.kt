package com.susuhan.travelpick.domain.travel.api

import com.susuhan.travelpick.domain.travel.dto.request.TravelCreateRequest
import com.susuhan.travelpick.domain.travel.dto.request.TravelUpdateRequest
import com.susuhan.travelpick.domain.travel.dto.response.TravelCreateResponse
import com.susuhan.travelpick.domain.travel.dto.response.TravelUpdateResponse
import com.susuhan.travelpick.domain.travel.service.TravelCommandService
import com.susuhan.travelpick.global.security.CustomUserDetails
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/travels")
@RestController
@Tag(name = "여행 관련 API")
class TravelController(
    private val travelCommandService: TravelCommandService
) {

    @Operation(
        summary = "새로운 여행지 생성",
        description = "새로운 여행지에 대한 데이터를 받아 새 여행지를 생성합니다.",
        security = [SecurityRequirement(name = "access-token")]
    )
    @PostMapping
    fun createTravel(
        @Valid @RequestBody travelCreateRequest: TravelCreateRequest
    ): ResponseEntity<TravelCreateResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(travelCommandService.createTravel(travelCreateRequest))
    }

    @Operation(
        summary = "여행지 수정",
        description = """
            여행지에 대한 데이터를 받아 여행지의 데이터를 수정합니다. 
            단, 여행지 데이터 수정은 여행지 생성자만 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @PutMapping("/{travelId}")
    fun updateTravel(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @Valid @RequestBody travelUpdateRequest: TravelUpdateRequest
    ): ResponseEntity<TravelUpdateResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(travelCommandService.updateTravel(customUserDetails.userId, travelId, travelUpdateRequest))
    }
}