package com.mohang.infrastructure.jwt.usecase

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.mohang.application.jwt.usecase.ExtractAuthMemberFromAuthTokenUseCase
import com.mohang.configuration.jwt.JwtConfiguration.Companion.MEMBER_ID_CLAIM
import com.mohang.configuration.jwt.JwtConfiguration.Companion.MEMBER_ROLE_CLAIM
import com.mohang.domain.enums.Role
import com.mohang.domain.jwt.AuthToken
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import com.mohang.infrastructure.jwt.usecase.properties.JwtProperties

/**
 * Created by ShinD on 2022/09/06.
 */
class ExtractAuthMemberFromAuthTokenUseCaseImpl(

    jwtProperties: JwtProperties,
): ExtractAuthMemberFromAuthTokenUseCase {


    // JWT 암호 알고리즘
    private val algorithm: Algorithm by lazy {
        Algorithm.HMAC512(jwtProperties.secretKey)
    }

    override fun command(authToken: AuthToken): AuthMemberPrinciple? {
        return try {

            val jwt          =   JWT.require(algorithm).build().verify(authToken.token)
            val id           =   jwt.getClaim(MEMBER_ID_CLAIM).toString().replace("\"", "")
            val role         =   jwt.getClaim(MEMBER_ROLE_CLAIM).toString().replace("\"", "")

            AuthMemberPrinciple(id = id.toLong(), role = Role.valueOf(role))
        }
        catch (ex: JWTVerificationException) {
            null // 토큰이 유효하지 않는 등의 예외 발생 시 -> null 반환
        }
    }
}