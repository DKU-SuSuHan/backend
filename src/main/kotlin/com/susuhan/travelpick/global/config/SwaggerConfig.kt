package com.susuhan.travelpick.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openAPI(@Value("\${springdoc.version}") appVersion: String): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Travel-Pick API Docs")
                    .description("Travel-Pick API 명세서")
                    .contact(Contact().name("LEE-SUJEONG").email("qalzm7351@gmail.com"))
                    .version(appVersion),
            )
            .components(
                Components()
                    .addSecuritySchemes(
                        "access-token",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("Bearer")
                            .bearerFormat("JWT"),
                    ),
            )
    }
}
