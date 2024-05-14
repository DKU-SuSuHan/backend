package com.susuhan.travelpick.global.kakao.client

import com.susuhan.travelpick.global.auth.dto.KakaoUserInfo
import com.susuhan.travelpick.global.config.openfeign.KakaoFeignConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "kakao-api-client",
    url = "https://kapi.kakao.com",
    configuration = [KakaoFeignConfig::class]
)
interface KakaoApiClient {

    @GetMapping(
        value = ["/v2/user/me"],
        headers = ["Content-type=application/x-www-form-urlencoded;charset=utf-8"]
    )
    fun getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) accessToken: String): KakaoUserInfo
}