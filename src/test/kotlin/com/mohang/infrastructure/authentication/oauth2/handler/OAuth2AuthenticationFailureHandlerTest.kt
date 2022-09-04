package com.mohang.infrastructure.authentication.oauth2.handler

import com.mohang.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.userdetails.UsernameNotFoundException
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/09/02.
 */
internal class OAuth2AuthenticationFailureHandlerTest {

    private val oauth2AuthenticationFailureHandler = OAuth2AuthenticationFailureHandler()

    @Test
    fun `OAuth2 인증 실패시 redirect url 로 error 전달`() {

        //given
        val redirectURI = "redirect"
        val req = MockHttpServletRequest()
        req.session!!.setAttribute(OAuth2AuthorizationRequestBasedOnSessionRepository.SESSION_REDIRECT_ATTR_NAME, redirectURI)

        val res = MockHttpServletResponse()

        val ex = UsernameNotFoundException("OAuth2 인증 예외 발생")

        //when
        oauth2AuthenticationFailureHandler.onAuthenticationFailure(req, res, ex)

        //then
        expectThat(res.redirectedUrl) {
            isEqualTo("redirect?error=${ex.message}")
        }
    }
}