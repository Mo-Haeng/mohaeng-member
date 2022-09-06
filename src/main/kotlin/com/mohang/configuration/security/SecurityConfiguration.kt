package com.mohang.configuration.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohang.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohang.application.jwt.usecase.ExtractAuthMemberFromAuthTokenUseCase
import com.mohang.configuration.security.enums.PermitAllURI
import com.mohang.domain.member.MemberPasswordEncoder
import com.mohang.infrastructure.authentication.exception.SendErrorAccessDeniedHandler
import com.mohang.infrastructure.authentication.exception.SendErrorAuthenticationEntryPoint
import com.mohang.infrastructure.authentication.jsonlogin.filter.JsonAuthenticationProcessingFilter
import com.mohang.infrastructure.authentication.jsonlogin.handler.JsonAuthenticationFailureHandler
import com.mohang.infrastructure.authentication.jsonlogin.handler.JsonAuthenticationSuccessHandler
import com.mohang.infrastructure.authentication.jsonlogin.provider.JsonAuthenticationProvider
import com.mohang.infrastructure.authentication.jsonlogin.provider.usecase.LoadMemberUseCase
import com.mohang.infrastructure.authentication.jwt.filter.JwtAuthenticationProcessingFilter
import com.mohang.infrastructure.authentication.jwt.handler.JwtAuthenticationFailureHandler
import com.mohang.infrastructure.authentication.jwt.manager.JwtAuthenticationManager
import com.mohang.infrastructure.authentication.oauth2.handler.OAuth2AuthenticationFailureHandler
import com.mohang.infrastructure.authentication.oauth2.handler.OAuth2AuthenticationSuccessHandler
import com.mohang.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository
import com.mohang.infrastructure.authentication.oauth2.userservice.OAuth2SignUpLoginMemberService
import com.mohang.infrastructure.authentication.oauth2.userservice.provider.OAuth2AttributesToMemberConverterProvider
import com.mohang.infrastructure.authentication.oauth2.userservice.provider.converter.GoogleToMemberConverter
import com.mohang.infrastructure.authentication.oauth2.userservice.provider.converter.KakaoToMemberConverter
import com.mohang.infrastructure.authentication.oauth2.userservice.provider.converter.NaverToMemberConverter
import com.mohang.infrastructure.authentication.oauth2.userservice.usecase.OAuth2SignUpUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

/**
 * Created by ShinD on 2022/08/31.
 */
@Configuration
class SecurityConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http {

            //== URL 권한 정보 설정 ==//
            authorizeHttpRequests {
                // set permit all uri
                PermitAllURI.values().map { value ->
                    when (value.method) {
                        //method가 null인 경우 -> 모든 메서드에 대해 허용
                        null -> authorize(AntPathRequestMatcher(value.uri), permitAll)
                        else -> authorize(AntPathRequestMatcher(value.uri, value.method.name), permitAll)
                    }
                }

                authorize(anyRequest, authenticated)
            }

            //== csrf 사용 X ==//
            csrf { disable() }

            //== httpBasic 사용 X ==//
            httpBasic { disable() }

            //== session 사용 X ==//
            sessionManagement { sessionCreationPolicy = STATELESS }

            // h2 사용할 수 있도록 설정
            headers { frameOptions { sameOrigin = true } }

            // OAuth2 로그인 사용
            oauth2Login {
                authorizationEndpoint {
                    /*
                     http://~~~~/{baseUri} 로 요청이 들어오면 OAuth2 인증을 수행한다
                     EX : http://localhost:9999/login/oauth2/authorization/kakao
                     */
                    baseUri = "/login/oauth2/authorization"
                    authorizationRequestRepository = OAuth2AuthorizationRequestBasedOnSessionRepository()
                }

                redirectionEndpoint {
                    // 로그인 API 등록 시 Redirect URL 설정과 동일해야 함
                    baseUri = "/*/oauth2/code/*"
                }

                // OAuth2 인증 성공 시 사용자 정보를 받아와 파싱하는 서비스
                userInfoEndpoint {
                    userService = oauth2SignUpLoginUserService()
                }

                authenticationSuccessHandler = oauth2AuthenticationSuccessHandler()
                authenticationFailureHandler = oauth2AuthenticationFailureHandler()
            }

            exceptionHandling {
                authenticationEntryPoint = sendErrorAuthenticationEntryPoint()
                accessDeniedHandler = sendErrorAccessDeniedHandler()
            }

            addFilterBefore<OAuth2AuthorizationRequestRedirectFilter>(jsonAuthenticationProcessingFilter())
            addFilterBefore<JsonAuthenticationProcessingFilter>(jwtAuthenticationProcessingFilter())
        }

        return http.build()
    }



    /**
     * OAuth2 로그인 사용자 정보 저장 관리
     */
    @Bean
    fun oauth2SignUpLoginUserService(
        oauth2SignUpUseCase: OAuth2SignUpUseCase? = null,
        oauth2AttributesToMemberConverterProvider: OAuth2AttributesToMemberConverterProvider? = null,
    ): OAuth2SignUpLoginMemberService {

        checkNotNull(oauth2SignUpUseCase) { "OAuth2SignUpUseCase is Null" }
        checkNotNull(oauth2AttributesToMemberConverterProvider) { "oauth2AttributesToMemberConverterProvider is Null" }

        return OAuth2SignUpLoginMemberService(
            signUpUseCase = oauth2SignUpUseCase,
            provider = oauth2AttributesToMemberConverterProvider,
        )

    }



    /**
     * OAuth2 서비스에서 받은 정보를 가지고 Member로 변환하는 Converter
     * OAuth2SignUpLoginUserService에서 사용
     *
     * OAuth2 서비스가 증가하면 더 추가할 수 있음
     */
    @Bean
    fun oauth2AttributesToMemberConverterProvider() : OAuth2AttributesToMemberConverterProvider {

        val kakaoToMemberConverter = KakaoToMemberConverter()
        val googleToMemberConverter = GoogleToMemberConverter()
        val naverToMemberConverter = NaverToMemberConverter()

        return OAuth2AttributesToMemberConverterProvider(
            listOf(kakaoToMemberConverter, googleToMemberConverter, naverToMemberConverter)
        )
    }



    /**
     * OAuth2 로그인 성공 시 후처리
     */
    @Bean
    fun oauth2AuthenticationSuccessHandler(
        authTokenCreateUseCase: AuthTokenCreateUseCase? = null,
    ): OAuth2AuthenticationSuccessHandler {

        checkNotNull(authTokenCreateUseCase) { "authTokenCreateUseCase is Null" }
        return OAuth2AuthenticationSuccessHandler(authTokenCreateUseCase)
    }



    /**
     * OAuth2 로그인 실패 시 후처리
     */
    @Bean
    fun oauth2AuthenticationFailureHandler(): OAuth2AuthenticationFailureHandler {
        return OAuth2AuthenticationFailureHandler()
    }



    /**
     * Json으로 로그인 진행하는 필터
     */
    @Bean
    fun jsonAuthenticationProcessingFilter(
        objectMapper: ObjectMapper? = null,
        jsonAuthenticationProvider: JsonAuthenticationProvider? = null,
        jsonAuthenticationSuccessHandler: JsonAuthenticationSuccessHandler? = null,
        jsonAuthenticationFailureHandler: JsonAuthenticationFailureHandler? = null,
    ): JsonAuthenticationProcessingFilter {

        checkNotNull(objectMapper) { "objectMapper is Null" }
        checkNotNull(jsonAuthenticationProvider) { "jsonAuthenticationProvider is Null" }
        checkNotNull(jsonAuthenticationSuccessHandler) { "jsonAuthenticationSuccessHandler is Null" }

        val filter = JsonAuthenticationProcessingFilter(loginUri = PermitAllURI.LOGIN.uri, objectMapper)
        filter.setAuthenticationManager(ProviderManager(jsonAuthenticationProvider))
        filter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler)
        filter.setAuthenticationFailureHandler(jsonAuthenticationFailureHandler)

        return filter
    }



    /**
     * Json으로 로그인 진행 시 사용자 인증정보 제공
     */
    @Bean
    fun jsonAuthenticationProvider(
        encoder: MemberPasswordEncoder,
        loginUserService: LoadMemberUseCase,
    ): JsonAuthenticationProvider = JsonAuthenticationProvider(encoder, loginUserService)



    /**
     * Json으로 로그인 성공 시 처리
     */
    @Bean
    fun jsonAuthenticationSuccessHandler(
        authTokenCreateUseCase: AuthTokenCreateUseCase? = null,
    ): JsonAuthenticationSuccessHandler {

        checkNotNull(authTokenCreateUseCase) { "authTokenCreateUseCase is Null" }
        return JsonAuthenticationSuccessHandler(authTokenCreateUseCase)
    }


    /**
     * Json으로 로그인 실패 시 처리
     */
    @Bean
    fun jsonAuthenticationFailureHandler(
        objectMapper: ObjectMapper? = null
    ): JsonAuthenticationFailureHandler {

        checkNotNull(objectMapper) { "objectMapper is Null" }
        return JsonAuthenticationFailureHandler(objectMapper)
    }



    /**
     * JWT로 인증 진행하는 필터
     */
    @Bean
    fun jwtAuthenticationProcessingFilter(
        jwtAuthenticationManager: JwtAuthenticationManager? = null,
        jwtAuthenticationFailureHandler: JwtAuthenticationFailureHandler? = null,
    ): JwtAuthenticationProcessingFilter {

        checkNotNull(jwtAuthenticationManager) { "jwtAuthenticationManager is null" }
        checkNotNull(jwtAuthenticationFailureHandler) { "jwtAuthenticationFailureHandler is null" }

        return JwtAuthenticationProcessingFilter(
            PermitAllURI.permitAllMap(),
            jwtAuthenticationManager,
            jwtAuthenticationFailureHandler,
        )
    }

    /**
     * JWT 에서 실제 인증 정보를 꺼내 인증을 수행하는 관리자
     */
    @Bean
    fun jwtAuthenticationManager(
        extractAuthMemberFromAuthTokenUseCase: ExtractAuthMemberFromAuthTokenUseCase
    ): JwtAuthenticationManager {

        checkNotNull(extractAuthMemberFromAuthTokenUseCase) { "extractAuthMemberFromAuthTokenUseCase is Null" }
        return JwtAuthenticationManager(extractAuthMemberFromAuthTokenUseCase)
    }


    /**
     * JWT 인증 실패 시 처리
     */
    @Bean
    fun jwtAuthenticationFailureHandler(
        objectMapper: ObjectMapper? = null
    ): JwtAuthenticationFailureHandler {

        checkNotNull(objectMapper) { "objectMapper is Null" }
        return JwtAuthenticationFailureHandler(objectMapper)
    }





    /**
     * 인증 실패 시 처리
     */
    @Bean
    fun sendErrorAccessDeniedHandler(
        objectMapper: ObjectMapper? = null,
    ): SendErrorAccessDeniedHandler {

        checkNotNull(objectMapper) { "objectMapper is null" }

        return SendErrorAccessDeniedHandler(objectMapper)
    }

    /**
     * 인가 실패 시 처리
     */
    @Bean
    fun sendErrorAuthenticationEntryPoint(
        objectMapper: ObjectMapper? = null,
    ): SendErrorAuthenticationEntryPoint {

        checkNotNull(objectMapper) { "objectMapper is null" }

        return SendErrorAuthenticationEntryPoint(objectMapper)
    }

    /**
     * https://velog.io/@gkdud583/HttpSecurity-WebSecurity%EC%9D%98-%EC%B0%A8%EC%9D%B4
     * WebSecurity - 인증,인가 모두 처리 X
     * HttpSecurity - antMatchers에 있는 endpoint에 대한 '인증'을 무시한다.
     */
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer
            = WebSecurityCustomizer { it.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**", "/favicon.ico/**") }


}