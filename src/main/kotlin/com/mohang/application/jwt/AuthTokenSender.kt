package com.mohang.application.jwt

import com.mohang.domain.jwt.AuthToken
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/02.
 */
class AuthTokenSender {

    companion object {
        const val QUERY_PARAM_NAME = "authToken"
        const val HEADER_NAME = "authToken"
        const val JSON_NAME = "authToken"
    }

    /**
     * redirect + queryString으로 AuthToken 전달
     */
    fun sendByRedirectQueryString(
        response: HttpServletResponse,
        redirectUrl: String,
        authToken: AuthToken
    ) {
        // redirectUrl 에 쿼리스트링 추가
        val redirectUrlWithParam = UriComponentsBuilder.fromUriString(redirectUrl)
            .queryParam(QUERY_PARAM_NAME, authToken.token)
            .build().toUriString()

        response.sendRedirect(redirectUrlWithParam)
    }

    /**
     * Header에 JWT 담아서 전달
     */
    fun sendByHeader(response: HttpServletResponse, authToken: AuthToken) {
        response.setHeader(HEADER_NAME, authToken.token)
    }

    /**
     * JSON에 JWT 담아서 전달
     */
    fun sendByJson(response: HttpServletResponse, authToken: AuthToken) {
        response.contentType = APPLICATION_JSON_VALUE
        response.writer.println(
            """
                {
                    "$JSON_NAME":"${authToken.token}"
                }
            """.trimIndent()
        )
        response.writer.flush()
    }
}