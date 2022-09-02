package com.mohang.infrastructure.jwt.usecase

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.algorithms.Algorithm.HMAC512
import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase.Companion.AUTH_TOKEN_SUBJECT
import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase.Companion.MEMBER_ID_CLAIM
import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase.Companion.MEMBER_ROLE_CLAIM
import com.mohang.domain.jwt.AuthToken
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import com.mohang.infrastructure.jwt.usecase.properties.JwtProperties
import java.lang.System.currentTimeMillis
import java.util.*
import java.util.concurrent.TimeUnit.DAYS
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * Created by ShinD on 2022/09/02.
 */

class AuthTokenCreateUseCaseImpl(

    jwtProperties: JwtProperties,

    ) : AuthTokenCreateUseCase {

    // JWT 암호 알고리즘
    private val algorithm: Algorithm by lazy {
        HMAC512(jwtProperties.secretKey)
    }

    // JWT authToken 만료일
    private val authTokenExpirationPeriodDay by lazy {
        jwtProperties.authTokenExpirationPeriodDay
    }

    override fun command(authPrinciple: AuthMemberPrinciple) =
        AuthToken(token = createAuthToken(authPrinciple))


    /**
     * AuthToken 생성
     */
    private fun createAuthToken(authPrinciple: AuthMemberPrinciple): String =
        JWT.create()
            .withSubject(AUTH_TOKEN_SUBJECT)
            .withClaim(MEMBER_ID_CLAIM, authPrinciple.id)
            .withClaim(MEMBER_ROLE_CLAIM, authPrinciple.role.name) // role은 반드시 하나임
            .withExpiresAt(
                Date(MILLISECONDS.convert(authTokenExpirationPeriodDay, DAYS).plus(currentTimeMillis()))
            )
            .sign(algorithm)
}