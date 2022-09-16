package com.mohaeng.infrastructure.authentication.oauth2.repo

import com.mohaeng.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository.Companion.SESSION_REDIRECT_ATTR_NAME
import io.mockk.every
import io.mockk.mockkClass
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

/**
 * Created by ShinD on 2022/09/02.
 */
internal class OAuth2AuthorizationRequestBasedOnSessionRepositoryTest{

    /**
     * saveAuthorizationRequest 세션 저장 테스트
     *
     *  removeAuthorizationRequest 테스트
     */

    private val oauth2AuthorizationRequestBasedOnSessionRepository = OAuth2AuthorizationRequestBasedOnSessionRepository()

    @Test
    fun `request url 저장 테스트`() {

        //given
        val oAuth2AuthorizationRequest: OAuth2AuthorizationRequest = mockkClass(OAuth2AuthorizationRequest::class)
        every { oAuth2AuthorizationRequest.state } returns "mock state"
        val redirectUrl = "redirect"
        val req = MockHttpServletRequest()
        req.setParameter("redirect_url", redirectUrl)
        val res = MockHttpServletResponse()


        //when
        oauth2AuthorizationRequestBasedOnSessionRepository.saveAuthorizationRequest(
            oAuth2AuthorizationRequest,
            req,
            res,
        )

        //then
        expectThat( req.session!!.getAttribute(SESSION_REDIRECT_ATTR_NAME) ) {
            isEqualTo(redirectUrl)
        }
        expectThat(req.session!!.getAttribute("OAuth2AuthorizationRequestBasedOnCookieRepository.AUTHORIZATION_REQUEST")) {
            isEqualTo(oAuth2AuthorizationRequest)
        }
    }


    @Test
    fun `removeAuthorizationRequest authorizationRequests 제거 테스트`() {

        //given
        val oAuth2AuthorizationRequest: OAuth2AuthorizationRequest = mockkClass(OAuth2AuthorizationRequest::class)
        every { oAuth2AuthorizationRequest.state } returns "mock state"
        val req = MockHttpServletRequest()
        req.setParameter(OAuth2ParameterNames.STATE, "mock state")

        req.getSession(true)!!.setAttribute("OAuth2AuthorizationRequestBasedOnCookieRepository.AUTHORIZATION_REQUEST", oAuth2AuthorizationRequest)
        val res = MockHttpServletResponse()


        //when
        oauth2AuthorizationRequestBasedOnSessionRepository.removeAuthorizationRequest(req, res)

        //then
        expectThat(
            req.session!!.getAttribute("OAuth2AuthorizationRequestBasedOnCookieRepository.AUTHORIZATION_REQUEST")
        ) {
            isNull()
        }
    }
}