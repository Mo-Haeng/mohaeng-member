package com.mohang.infrastructure.auth.oauth2.repo

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/02.
 */

class OAuth2AuthorizationRequestBasedOnSessionRepository :
    AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    companion object {
        private const val DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME ="OAuth2AuthorizationRequestBasedOnCookieRepository.AUTHORIZATION_REQUEST"
        const val SESSION_REDIRECT_ATTR_NAME = "REDIRECT_URL"
    }

    private val sessionAttrName = DEFAULT_AUTHORIZATION_REQUEST_ATTR_NAME
    private val redirectUrlParameterName = "redirect_url"



    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {

        val stateParameter = getStateParameter(request) ?: return null

        val authorizationRequests: Map<String, OAuth2AuthorizationRequest> = getAuthorizationRequests(request)
        return authorizationRequests[stateParameter]
    }





    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) {

        if (authorizationRequest == null) {
            this.removeAuthorizationRequest(request, response)
            return
        }

        checkNotNull(authorizationRequest.state) { "authorizationRequest.state cannot be empty" }

        request.session.setAttribute(SESSION_REDIRECT_ATTR_NAME, request.getParameter(redirectUrlParameterName))
        request.session.setAttribute(sessionAttrName, authorizationRequest)
    }


    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): OAuth2AuthorizationRequest? {

        val authorizationReqs = getAuthorizationRequests(request)

        // state에 해당하는 OAuth2AuthorizationRequest를 제거하면서 가져온다
        return  authorizationReqs.remove(getStateParameter(request) ?: return null)

    }


    private fun getStateParameter(request: HttpServletRequest): String? {
        return request.getParameter(OAuth2ParameterNames.STATE)
    }



    private fun getAuthorizationRequests(request: HttpServletRequest): MutableMap<String, OAuth2AuthorizationRequest> {

        val sessionAttributeValue = request.getSession(false)?.getAttribute(sessionAttrName)
                                    ?: return HashMap()

        sessionAttributeValue as OAuth2AuthorizationRequest

        val authorizationRequests: MutableMap<String, OAuth2AuthorizationRequest> = HashMap(1)
        authorizationRequests[sessionAttributeValue.state] = sessionAttributeValue
        return authorizationRequests
    }


    @Deprecated("Deprecated in Java")
    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        throw RuntimeException("removeAuthorizationRequest is Deprecated")
    }

}