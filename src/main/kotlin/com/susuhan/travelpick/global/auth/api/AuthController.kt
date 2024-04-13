package com.susuhan.travelpick.global.auth.api

import com.susuhan.travelpick.global.auth.dto.request.KakaoLoginRequest
import com.susuhan.travelpick.global.auth.dto.response.TokenResponse
import com.susuhan.travelpick.global.kakao.service.KakaoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/v1/auth")
@RestController
@Tag(name = "소셜 로그인 관련 API")
class AuthController(
    private val kakaoService: KakaoService
) {

    @Operation(
        summary = "카카오 소셜 로그인 API",
        description = "카카오 액세스 토큰을 전달 받아 카카오 소셜 로그인을 진행합니다."
    )
    @PostMapping("/login/kakao")
    fun kakaoLogin(@Valid @RequestBody request: KakaoLoginRequest): ResponseEntity<TokenResponse> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(kakaoService.login(request))
    }
}