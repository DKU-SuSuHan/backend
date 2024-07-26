package com.susuhan.travelpick.global.config

import com.susuhan.travelpick.global.log.LogFilter
import jakarta.servlet.Filter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun logApiFilter(): FilterRegistrationBean<Filter> {
        return FilterRegistrationBean<Filter>().apply {
            this.filter = LogFilter()
            this.order = -101
            this.setName("LogFilter")
            this.addUrlPatterns("/*")
        }
    }
}
