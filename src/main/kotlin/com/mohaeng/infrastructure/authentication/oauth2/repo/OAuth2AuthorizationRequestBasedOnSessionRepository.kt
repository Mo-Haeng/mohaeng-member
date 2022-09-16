package com.mohaeng.infrastructure.authentication.oauth2.repo

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

    /**
     * 사용되지 않는다
     */
    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {

        val stateParameter = getStateParameter(request) ?: return null

        val authorizationRequests: Map<String, OAuth2AuthorizationRequest> = getAuthorizationRequests(request)
        return authorizationRequests[stateParameter]
    }

    /**
     * oauth2 서비스에 요청을 보내기 전 정보를 저장한다.
     *
     * authorizationGrantType이 authorization_code인 경우 실행
     * OAuth2AuthorizationRequestRedirectFilter.doFilterInternal() 내부의 sendRedirectForAuthorization()에서 실행
     */
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


    /**
     * oauth2 서비스에서 redirect 당했을 때 실행된다.
     * 이전 saveAuthorizationRequest()에서 저장한 정보를 제거하면서 가져온다
     */
    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): OAuth2AuthorizationRequest? {

        val authorizationReqs = getAuthorizationRequests(request)

        // state에 해당하는 OAuth2AuthorizationRequest를 제거하면서 가져온다
        val originalRequest = authorizationReqs.remove(getStateParameter(request) ?: return null)

        with(authorizationReqs) {
            when {
                isEmpty()                       -> request.session.removeAttribute(sessionAttrName)
                authorizationReqs.size == 1     -> request.session.setAttribute(sessionAttrName, authorizationReqs.values.iterator().next())
                else                            -> request.session.setAttribute(sessionAttrName, authorizationReqs)
            }
        }

        return originalRequest
    }


    private fun getAuthorizationRequests(request: HttpServletRequest): MutableMap<String, OAuth2AuthorizationRequest> {

        val sessionAttributeValue = request.getSession(false)?.getAttribute(sessionAttrName)
                                    ?: return HashMap()

        sessionAttributeValue as OAuth2AuthorizationRequest

        val authorizationRequests: MutableMap<String, OAuth2AuthorizationRequest> = HashMap(1)
        authorizationRequests[sessionAttributeValue.state] = sessionAttributeValue
        return authorizationRequests
    }


    private fun getStateParameter(request: HttpServletRequest): String? {
        return request.getParameter(OAuth2ParameterNames.STATE)
    }


    @Deprecated("Deprecated in Java")
    override fun removeAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        throw RuntimeException("removeAuthorizationRequest is Deprecated")
    }

}