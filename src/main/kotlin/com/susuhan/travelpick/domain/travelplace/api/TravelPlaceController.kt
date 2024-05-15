package com.susuhan.travelpick.domain.travelplace.api

import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceCreateRequest
import com.susuhan.travelpick.domain.travelplace.dto.request.TravelPlaceUpdateRequest
import com.susuhan.travelpick.domain.travelplace.dto.response.ConfirmTravelPlaceListResponse
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceCreateResponse
import com.susuhan.travelpick.domain.travelplace.dto.response.TravelPlaceUpdateResponse
import com.susuhan.travelpick.domain.travelplace.service.TravelPlaceCommandService
import com.susuhan.travelpick.domain.travelplace.service.TravelPlaceQueryService
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
    private val travelPlaceCommandService: TravelPlaceCommandService,
    private val travelPlaceQueryService: TravelPlaceQueryService
) {

    @Operation(
        summary = "여행 장소 수기 추가",
        description = """
            진행 중인 여행지에 대해 확정된 장소를 수기로 작성 받아 추가합니다.
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @PostMapping("/{travelId}/places")
    fun createByHand(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @Valid @RequestBody travelPlaceCreateRequest: TravelPlaceCreateRequest
    ): ResponseEntity<TravelPlaceCreateResponse> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(travelPlaceCommandService.createByHand(
                customUserDetails.getUserId(), travelId, travelPlaceCreateRequest
            ))
    }

    @Operation(
        summary = "여행 장소 수정",
        description = """
            여행지 장소에 대한 데이터를 받아 여행지 장소의 데이터를 수정합니다. 
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @PutMapping("/{travelId}/places/{travelPlaceId}")
    fun update(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @PathVariable(name = "travelPlaceId") travelPlaceId: Long,
        @Valid @RequestBody travelPlaceUpdateRequest: TravelPlaceUpdateRequest
    ): ResponseEntity<TravelPlaceUpdateResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(travelPlaceCommandService.update(
                customUserDetails.getUserId(), travelId, travelPlaceId, travelPlaceUpdateRequest
            ))
    }

    @Operation(
        summary = "여행 장소 삭제",
        description = """
            여행 장소의 PK를 전달받아 해당 여행지를 삭제합니다. 
            단, 해당 여행지에 대해 주도자 역할을 가진 사용자만 요청 가능합니다.
        """,
        security = [SecurityRequirement(name = "access-token")]
    )
    @DeleteMapping("/{travelId}/places/{travelPlaceId}")
    fun softDelete(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @PathVariable(name = "travelPlaceId") travelPlaceId: Long
    ): ResponseEntity<Unit> {
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(travelPlaceCommandService.softDelete(
                customUserDetails.getUserId(), travelId, travelPlaceId
            ))
    }

    @Operation(
        summary = "특정 여행 날짜의 확정된 여행 장소 목록 조회",
        description = "진행 중인 특정 여행 날짜를 전달 받아 확정된 여행 장소 목록 조회합니다.",
        security = [SecurityRequirement(name = "access-token")]
    )
    @GetMapping("/{travelId}/places")
    fun getConfirmPlaceList(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable(name = "travelId") travelId: Long,
        @RequestParam(name = "travelDay") travelDay: Int
    ): ResponseEntity<ConfirmTravelPlaceListResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(travelPlaceQueryService.getConfirmPlaceList(
                customUserDetails.getUserId(), travelId, travelDay
            ))
    }
}