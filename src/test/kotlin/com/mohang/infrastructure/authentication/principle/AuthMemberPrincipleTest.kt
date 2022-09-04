package com.mohang.infrastructure.authentication.principle

import com.mohang.fixture.MemberFixture
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.security.core.AuthenticatedPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User
import strikt.api.expectThat
import strikt.assertions.isSameInstanceAs
import strikt.assertions.isTrue

/**
 * Created by ShinD on 2022/09/02.
 */
internal class AuthMemberPrincipleTest {

    /**
     * AuthMemberPrinciple
     *
     * OAuth2User, UserDetails 할당되는가
     *
     * 이외 모든 메서드 잘 작동하는가
     */
    @Test
    fun `OAuth2User, UserDetails 할당 가능`() {

        //given
        val authMemberPrinciple = MemberFixture.authBasicMemberPrinciple()

        //when
        expectThat(OAuth2User::class.java.isAssignableFrom(authMemberPrinciple::class.java)) {
           isTrue()
        }

        //then
        expectThat(UserDetails::class.java.isAssignableFrom(authMemberPrinciple::class.java)) {
            isTrue()
        }
    }
}