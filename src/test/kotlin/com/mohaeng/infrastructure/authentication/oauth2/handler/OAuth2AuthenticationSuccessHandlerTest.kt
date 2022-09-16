package com.mohaeng.infrastructure.authentication.oauth2.handler

import com.mohaeng.application.jwt.AuthTokenSender
import com.mohaeng.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohaeng.domain.enums.OAuth2Type
import com.mohaeng.domain.enums.Role
import com.mohaeng.domain.jwt.AuthToken
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.infrastructure.authentication.jsonlogin.authentication.JsonUsernamePasswordToken
import com.mohaeng.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple
import io.mockk.*
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
internal class OAuth2AuthenticationSuccessHandlerTest {

    private val authTokenCreateUseCase = mockkClass(AuthTokenCreateUseCase::class)
    private val authTokenSender = mockkClass(AuthTokenSender::class)

    private val oauth2AuthenticationSuccessHandler by lazy {
        OAuth2AuthenticationSuccessHandler(authTokenCreateUseCase, authTokenSender)
    }

    @Test
    fun `OAuth2 로그인 성공 시 redirect url에 query String으로 AuthToken 전달`() {

        //given
        every { authTokenCreateUseCase.command(any()) } returns AuthToken("sample")
        every { authTokenSender.sendByRedirectQueryString(any(), any(), any()) } just runs

        val redirectURI = "redirect"
        val req = MockHttpServletRequest()
        req.session!!.setAttribute(OAuth2AuthorizationRequestBasedOnSessionRepository.SESSION_REDIRECT_ATTR_NAME, redirectURI)

        val res = MockHttpServletResponse()
        val authMemberPrinciple =
            AuthMemberPrinciple(
                id = 1L,
                oauth2LoginId = OAuth2LoginId(OAuth2Type.NONE, "username"),
                role = Role.BASIC
            )
        val authentication = JsonUsernamePasswordToken(authMemberPrinciple, null, role = authMemberPrinciple.role)

        //when
        oauth2AuthenticationSuccessHandler.onAuthenticationSuccess(req, res, authentication)

        //then
        verify (exactly = 1) { authTokenCreateUseCase.command(any()) }
        verify (exactly = 1) { authTokenSender.sendByRedirectQueryString(any(), redirectURI, any()) }
    }
}