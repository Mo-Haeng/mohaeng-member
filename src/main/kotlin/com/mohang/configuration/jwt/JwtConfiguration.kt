package com.mohang.configuration.jwt

import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohang.application.jwt.usecase.ExtractAuthMemberFromAuthTokenUseCase
import com.mohang.infrastructure.jwt.usecase.AuthTokenCreateUseCaseImpl
import com.mohang.infrastructure.jwt.usecase.ExtractAuthMemberFromAuthTokenUseCaseImpl
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


    companion object {

        const val AUTH_TOKEN_SUBJECT = "AuthToken"
        const val MEMBER_ID_CLAIM = "memberId"
        const val MEMBER_ROLE_CLAIM = "memberRole"
    }
    @Bean
    fun authTokenCreateUseCaseImpl(jwtProperties: JwtProperties): AuthTokenCreateUseCase {
        return AuthTokenCreateUseCaseImpl(jwtProperties)
    }

    @Bean
    fun extractAuthMemberFromAuthTokenUseCase(jwtProperties: JwtProperties): ExtractAuthMemberFromAuthTokenUseCase {
        return ExtractAuthMemberFromAuthTokenUseCaseImpl(jwtProperties)
    }
}