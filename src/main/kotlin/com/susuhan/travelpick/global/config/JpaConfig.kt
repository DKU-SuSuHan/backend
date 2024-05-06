package com.susuhan.travelpick.global.config

import com.susuhan.travelpick.global.security.CustomUserDetails
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@EnableJpaAuditing
@Configuration
class JpaConfig {

    @Bean
    fun auditorAware() = AuditorAware {
        Optional.ofNullable(SecurityContextHolder.getContext())
            .map { it.authentication }
            .filter { it.isAuthenticated && !it.principal.equals("anonymousUser") }
            .map { it.principal as CustomUserDetails }
            .map { it.userId }
    }
}