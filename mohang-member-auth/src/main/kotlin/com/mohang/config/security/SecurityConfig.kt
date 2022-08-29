package com.mohang.config.security

import com.mohang.domain.MemberPasswordEncoder
import com.mohang.infrastructure.security.SecurityPasswordEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Created by ShinD on 2022/08/30.
 */
@Configuration
class SecurityConfig {

    @Bean
    fun memberPasswordEncoder(): MemberPasswordEncoder {
        return SecurityPasswordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }
}