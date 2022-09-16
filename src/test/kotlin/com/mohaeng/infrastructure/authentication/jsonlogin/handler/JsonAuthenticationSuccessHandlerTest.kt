package com.mohaeng.infrastructure.authentication.jsonlogin.handler

import com.mohaeng.application.jwt.AuthTokenSender
import com.mohaeng.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohaeng.domain.enums.OAuth2Type.NONE
import com.mohaeng.domain.enums.Role
import com.mohaeng.domain.jwt.AuthToken
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.infrastructure.authentication.jsonlogin.authentication.JsonUsernamePasswordToken
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
internal class JsonAuthenticationSuccessHandlerTest {


    private val authTokenCreateUseCase = mockkClass(AuthTokenCreateUseCase::class)
    private val authTokenSender = mockkClass(AuthTokenSender::class)

    private val jsonAuthenticationSuccessHandler by lazy {
        JsonAuthenticationSuccessHandler(authTokenCreateUseCase, authTokenSender)
    }

    @Test
    fun `json 로그인 성공 시 header, body에 auth token 전달`() {

        //given
        every { authTokenCreateUseCase.command(any()) } returns AuthToken("sample")
        every { authTokenSender.sendByJson(any(), any()) } just runs
        every { authTokenSender.sendByHeader(any(), any()) } just runs

        val req = MockHttpServletRequest()
        val res = MockHttpServletResponse()
        val authMemberPrinciple =
            AuthMemberPrinciple(
                id = 1L,
                oauth2LoginId = OAuth2LoginId(NONE, "username"),
                role = Role.BASIC
            )
        val authentication = JsonUsernamePasswordToken(authMemberPrinciple, null, role = authMemberPrinciple.role)

        //when
        jsonAuthenticationSuccessHandler.onAuthenticationSuccess(req, res, authentication)

        //then
        verify (exactly = 1) { authTokenCreateUseCase.command(any()) }
        verify (exactly = 1) { authTokenSender.sendByJson(any(), any()) }
        verify (exactly = 1) { authTokenSender.sendByHeader(any(), any()) }
    }
}