package com.shddx.gateway

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.OAuth2ResourceServerSpec
import org.springframework.security.web.server.SecurityWebFilterChain

private const val EUREKA_PATH_MATCHER = "/eureka/**"

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun springSecurityFilterChain(serverHttpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        return serverHttpSecurity.csrf().disable()
            .authorizeExchange { exchange ->
                exchange
                    .pathMatchers(EUREKA_PATH_MATCHER).permitAll()
                    .anyExchange().authenticated()
            }.oauth2ResourceServer(OAuth2ResourceServerSpec::jwt)
            .build()
    }
}