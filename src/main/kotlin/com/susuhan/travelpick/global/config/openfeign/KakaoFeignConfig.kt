package com.susuhan.travelpick.global.config.openfeign

import feign.codec.Encoder
import feign.codec.ErrorDecoder
import feign.form.spring.SpringFormEncoder
import org.springframework.beans.factory.ObjectFactory
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KakaoFeignConfig {

    @Bean
    fun encoder(converters: ObjectFactory<HttpMessageConverters?>?): Encoder? {
        return SpringFormEncoder(SpringEncoder(converters))
    }

    @Bean
    fun kakaoErrorDecoder(): ErrorDecoder? {
        return KakaoFeignError()
    }
}
