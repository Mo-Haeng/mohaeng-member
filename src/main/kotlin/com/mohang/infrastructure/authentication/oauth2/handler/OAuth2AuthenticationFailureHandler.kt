package com.mohang.infrastructure.authentication.oauth2.handler

import com.mohang.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository.Companion.SESSION_REDIRECT_ATTR_NAME
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2AuthenticationFailureHandler : AuthenticationFailureHandler {



    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {

        var redirectUri = request.getSession(false).getAttribute(SESSION_REDIRECT_ATTR_NAME) as String
        request.session.invalidate()

        redirectUri = UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("error", exception.localizedMessage)
            .build().toUriString();

        response.sendRedirect(redirectUri)
    }
}