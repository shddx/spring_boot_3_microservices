package com.shddx.discoveryservice

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig(
    @Value("\${eureka.username}") val username: String,
    @Value("\${eureka.password}") val password: String
) {


    @Bean
    fun authenticationProvider(
        passwordEncoder: PasswordEncoder
    ): UserDetailsManager {
        val user: UserDetails = User.builder()
            .passwordEncoder(passwordEncoder::encode)
            .username(username)
            .password(password)
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        val encoders = mutableMapOf<String, PasswordEncoder>("bcrypt" to BCryptPasswordEncoder())
        return DelegatingPasswordEncoder("bcrypt", encoders)
    }

    @Bean
    fun securityFilterChain(
        httpSecurity: HttpSecurity
    ): SecurityFilterChain {
        return httpSecurity
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests.anyRequest().authenticated()
            }.httpBasic(withDefaults())
            .csrf().disable()
            .build()
    }
}