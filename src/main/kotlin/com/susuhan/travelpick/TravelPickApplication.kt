package com.susuhan.travelpick

import com.susuhan.travelpick.global.properties.JwtProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(JwtProperties::class)
@SpringBootApplication
class TravelPickApplication

fun main(args: Array<String>) {
    runApplication<TravelPickApplication>(*args)
}
