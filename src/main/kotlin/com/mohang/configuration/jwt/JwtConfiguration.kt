package com.mohang.configuration.jwt

import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohang.infrastructure.jwt.usecase.AuthTokenCreateUseCaseImpl
import com.mohang.infrastructure.jwt.usecase.properties.JwtProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by ShinD on 2022/09/02.
 */
@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class JwtConfiguration {

    @Bean
    fun authTokenCreateUseCaseImpl(jwtProperties: JwtProperties, ): AuthTokenCreateUseCase {
        return AuthTokenCreateUseCaseImpl(jwtProperties)
    }
}