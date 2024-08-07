package com.susuhan.travelpick.global.config

import com.susuhan.travelpick.global.security.JwtAuthenticationFilter
import com.susuhan.travelpick.global.security.JwtExceptionFilter
import com.susuhan.travelpick.global.security.handler.JwtAccessDeniedHandler
import com.susuhan.travelpick.global.security.handler.JwtAuthenticationEntryPoint
import feign.Request.HttpMethod
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtExceptionFilter: JwtExceptionFilter,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain = http
        .httpBasic { it.disable() }
        .formLogin { it.disable() }
        .csrf { it.disable() }
        .sessionManagement {
                configurer ->
            configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .cors { it.configurationSource(corsConfigurationSource()) }
        .authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/api/v1/auth/login/**").permitAll()
                .requestMatchers("/api/v1/auth/tokens/renewal").permitAll()
                .anyRequest().authenticated()
        }
        .exceptionHandling { exception ->
            exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)
        }
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter::class.java)
        .build()

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val corsConfig = CorsConfiguration().apply {
            allowCredentials = true
            allowedOriginPatterns = listOf("*") // TODO: 프론트엔드 배포 후 구체적인 도메인으로 변경
            allowedMethods = listOf(
                HttpMethod.GET.name,
                HttpMethod.POST.name,
                HttpMethod.PATCH.name,
                HttpMethod.PUT.name,
                HttpMethod.DELETE.name,
            )
            allowedHeaders = listOf("*")
            exposedHeaders = listOf("*")
        }

        val corsConfigSource = UrlBasedCorsConfigurationSource().apply {
            registerCorsConfiguration("/**", corsConfig)
        }

        return corsConfigSource
    }
}
