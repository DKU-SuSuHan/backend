package com.susuhan.travelpick.global.config

import com.susuhan.travelpick.global.properties.JwtProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@EnableConfigurationProperties(
    JwtProperties::class,
)
@Configuration
class PropertiesConfig
