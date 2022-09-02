package com.mohang.application.jwt

import com.mohang.application.jwt.AuthTokenSender.Companion.HEADER_NAME
import com.mohang.domain.jwt.AuthToken
import org.junit.jupiter.api.Test
import org.springframework.mock.web.MockHttpServletResponse
import strikt.api.expectThat
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/09/02.
 */
internal class AuthTokenSenderTest {

    /**
     * redirect + queryString으로 AuthToken 전달
     *
     * Header에 JWT 담아서 전달
     *
     * JSON에 JWT 담아서 전달
     *
     */

    private val authTokenSender: AuthTokenSender = AuthTokenSender()


    @Test
    fun `redirect + queryString으로 AuthToken 전달 테스트`() {

        //given
        val response = MockHttpServletResponse()
        val redirectUrl = "http://example.com"
        val authToken = AuthToken("example token")

        //when
        authTokenSender.sendByRedirectQueryString(response, redirectUrl, authToken)

        //then
        expectThat(response.redirectedUrl)
            .isEqualTo("${redirectUrl}?${AuthTokenSender.QUERY_PARAM_NAME}=${authToken.token}")
    }


    @Test
    fun `Header에 AuthToken 전달 테스트`() {

        //given
        val response = MockHttpServletResponse()
        val authToken = AuthToken("example token")

        //when
        authTokenSender.sendByHeader(response, authToken)

        //then
        expectThat(response.getHeader(HEADER_NAME))
            .isEqualTo(authToken.token)
    }

    @Test
    fun `JSON에 AuthToken 전달 테스트`() {

        //given
        val response = MockHttpServletResponse()
        val authToken = AuthToken("example token")

        //when
        authTokenSender.sendByJson(response, authToken)

        //then
        expectThat(response.contentAsString.trimIndent())
            .isEqualTo("""
                {
                    "${AuthTokenSender.JSON_NAME}":"${authToken.token}"
                }
            """.trimIndent())
    }
}