package com.susuhan.travelpick.global.config.openfeign

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@EnableFeignClients(basePackages = ["com.susuhan.travelpick"])
@Configuration
class FeignConfig
