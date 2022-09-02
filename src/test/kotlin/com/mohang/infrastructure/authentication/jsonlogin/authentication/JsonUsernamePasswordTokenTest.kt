package com.mohang.infrastructure.authentication.jsonlogin.authentication

import com.mohang.domain.enums.OAuth2Type.NONE
import com.mohang.domain.enums.Role
import com.mohang.domain.member.OAuth2LoginId
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import strikt.assertions.isTrue

/**
 * Created by ShinD on 2022/09/01.
 */
internal class JsonUsernamePasswordTokenTest {


    @Test
    fun `인증 전 생성자 테스트`() {

        //given
        val username = "username"
        val password = "password"

        //when
        expectThat(
            JsonUsernamePasswordToken(principal = username, credentials = password).isAuthenticated
        ) //then
            .isFalse()
    }

    @Test
    fun `인증 후 생성자 테스트`() {

        //given
        val username = "username"
        val password = "password"
        val oauth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username)
        val role = Role.BASIC
        val authMemberPrinciple = AuthMemberPrinciple(id = 1L, password = password, oauth2LoginId = oauth2LoginId, role = role)

        //when
        expectThat(
            JsonUsernamePasswordToken(principal = authMemberPrinciple, credentials = password, role = role).isAuthenticated
        ) //then
            .isTrue()
    }

    @Test
    fun `getPrinciple() 반환 테스트`() {

        //given
        val username = "username"
        val password = "password"
        val oauth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username)
        val role = Role.BASIC
        val authMemberPrinciple = AuthMemberPrinciple(id = 1L, password = password, oauth2LoginId = oauth2LoginId, role = role)

        //when
        expectThat(
            JsonUsernamePasswordToken(principal = authMemberPrinciple, credentials = password, role = role).principal
        ) //then
            .isEqualTo(authMemberPrinciple)
    }

    @Test
    fun `getCredentials() 반환 테스트`() {

        //given
        val username = "username"
        val password = "password"
        val oauth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username)
        val role = Role.BASIC
        val authMemberPrinciple = AuthMemberPrinciple(id = 1L, password = password, oauth2LoginId = oauth2LoginId, role = role)

        //when
        expectThat(
            JsonUsernamePasswordToken(principal = authMemberPrinciple, credentials = password, role = role).credentials
        ) //then
            .isEqualTo(password)
    }

    @Test
    fun `eraseCredentials() 테스트`() {

        //given
        val username = "username"
        val password = "password"
        val oauth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username)
        val role = Role.BASIC
        val authMemberPrinciple = AuthMemberPrinciple(id = 1L, password = password, oauth2LoginId = oauth2LoginId, role = role)

        val jupt =
            JsonUsernamePasswordToken(principal = authMemberPrinciple, credentials = password, role = role)

        jupt.eraseCredentials()

        //when
        expectThat(jupt.credentials) //then
            .isEqualTo(null)
    }
}