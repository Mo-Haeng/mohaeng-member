package com.mohaeng.infrastructure.jwt.usecase

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mohaeng.configuration.jwt.JwtConfiguration.Companion.MEMBER_ID_CLAIM
import com.mohaeng.configuration.jwt.JwtConfiguration.Companion.MEMBER_ROLE_CLAIM

import com.mohaeng.domain.enums.OAuth2Type
import com.mohaeng.domain.enums.Role
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple
import com.mohaeng.infrastructure.jwt.usecase.properties.JwtProperties
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/09/02.
 */
internal class AuthTokenCreateUseCaseImplTest {

    private val jwtProperties: JwtProperties = JwtProperties(
        secretKey = "ZG9uZ2h1bi1zaGFycC1kYnJ1YS13ZWItcHJvamVjdC11c2luZy1qd3Qtc2VjcmV0LURvbmdodW4tc3ByaW5nLWJvb3Qtand0LWJhY2stZW5kLWFuZC1qcy1jb2xsYWJv",
        authTokenExpirationPeriodDay = 30,
        refreshTokenExpirationPeriodDay = 300,
    )
    private val authTokenCreateUseCaseImpl = AuthTokenCreateUseCaseImpl(jwtProperties)

    @Test
    fun `AuthToken 생성 기능 테스트`() {

        //given
        val memberId = 1L
        val memberRole = Role.BASIC
        val authMemberPrinciple =
            AuthMemberPrinciple(
                id = memberId,
                oauth2LoginId = OAuth2LoginId(OAuth2Type.NONE, "username"),
                role = memberRole
            )

        //when
        val authToken = authTokenCreateUseCaseImpl.command(authMemberPrinciple)

        //then
        val jwt =  JWT.require(Algorithm.HMAC512(jwtProperties.secretKey)).build().verify(authToken.token)

        with(jwt) {
            expectThat(getClaim(MEMBER_ID_CLAIM).toString().replace("\"", "")) {
                isEqualTo(memberId.toString())
            }
            expectThat(getClaim(MEMBER_ROLE_CLAIM).toString().replace("\"", "")) {
                isEqualTo(memberRole.toString())
            }
        }
    }
}