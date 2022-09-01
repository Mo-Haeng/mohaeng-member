package com.mohang.infrastructure.auth.oauth2.handler

import com.mohang.infrastructure.auth.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository.Companion.SESSION_REDIRECT_ATTR_NAME
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.util.UriComponentsBuilder
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2AuthenticationSuccessHandler : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication,
    ) {

        var redirectUri = request.getSession(false).getAttribute(SESSION_REDIRECT_ATTR_NAME) as String
        request.session.invalidate()


        redirectUri = UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("accessToken", "AccessToken")
            .build().toUriString();

        response.sendRedirect(redirectUri)
    }
}