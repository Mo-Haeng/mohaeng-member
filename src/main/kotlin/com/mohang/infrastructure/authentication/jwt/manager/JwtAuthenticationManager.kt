package com.mohang.infrastructure.authentication.jwt.manager

import com.mohang.application.jwt.usecase.ExtractAuthMemberFromAuthTokenUseCase
import com.mohang.domain.jwt.AuthToken
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import javax.security.auth.message.AuthException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/06.
 */
class JwtAuthenticationManager (

    private val extractAuthMemberFromAuthTokenUseCase: ExtractAuthMemberFromAuthTokenUseCase,

    ) {

    companion object {
        const val AUTH_TOKEN_HEADER_NAME = "Authorization"
        const val AUTH_TOKEN_HEADER_PREFIX = "Bearer "
    }

    private val log = KotlinLogging.logger {  }





    /**
     * AccessToken과 RefreshToken을 가지고 인증 처리
     */
    fun authenticateWithTokens(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        log.info { "JWT 인증 진행" }

        // 토큰이 존재하지 않는 경우 예외
        val authToken = extractToken(request) ?: throw AuthException("Auth Token이 존재하지 않습니다.")

        // UserDetails 이 없는 경우 오류
        val authMember = extractAuthMemberFromAuthTokenUseCase.command(authToken) ?: throw AuthException("유효하지 않은 Auth Token입니다.")

        // 인증 성공
        successAuthentication(authMember)

        // 이후 필터 진행
        chain.doFilter(request, response)
    }


    /**
     * Http Request 정보로부터 토큰이 있다면 추출
     */
    fun extractToken(request: HttpServletRequest): AuthToken? {

        //AUTH_TOKEN_HEADER_NAME(Authorization) 이 없는 경우 Null
        val accessTokenValue = request.getHeader(AUTH_TOKEN_HEADER_NAME) ?: return null

        //AUTH_TOKEN_HEADER_PREFIX(Bearer )로 시작하지 않는 경우 Null
        if (!accessTokenValue.startsWith(AUTH_TOKEN_HEADER_PREFIX)) return null

        //accessToken 추출 (Bearer 제거)
        val accessToken = accessTokenValue.replace(AUTH_TOKEN_HEADER_PREFIX, "").trim()

        return AuthToken(token = accessToken)
    }



    /**
     * 인증 성공 처리
     */
    private fun successAuthentication(authMemberPrinciple: AuthMemberPrinciple) {

        val context: SecurityContext = SecurityContextHolder.createEmptyContext()

        context.authentication = UsernamePasswordAuthenticationToken(authMemberPrinciple, null, authMemberPrinciple.authorities)

        SecurityContextHolder.setContext(context)
    }
}