package com.mohaeng.configuration.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohaeng.application.jwt.usecase.AuthTokenCreateUseCase
import com.mohaeng.application.jwt.usecase.ExtractAuthMemberFromAuthTokenUseCase
import com.mohaeng.configuration.security.enums.PermitAllURI
import com.mohaeng.domain.member.MemberPasswordEncoder
import com.mohaeng.infrastructure.authentication.exception.SendErrorAccessDeniedHandler
import com.mohaeng.infrastructure.authentication.exception.SendErrorAuthenticationEntryPoint
import com.mohaeng.infrastructure.authentication.jsonlogin.filter.JsonAuthenticationProcessingFilter
import com.mohaeng.infrastructure.authentication.jsonlogin.handler.JsonAuthenticationFailureHandler
import com.mohaeng.infrastructure.authentication.jsonlogin.handler.JsonAuthenticationSuccessHandler
import com.mohaeng.infrastructure.authentication.jsonlogin.provider.JsonAuthenticationProvider
import com.mohaeng.infrastructure.authentication.jsonlogin.provider.usecase.LoadMemberUseCase
import com.mohaeng.infrastructure.authentication.jwt.filter.JwtAuthenticationProcessingFilter
import com.mohaeng.infrastructure.authentication.jwt.handler.JwtAuthenticationFailureHandler
import com.mohaeng.infrastructure.authentication.jwt.manager.JwtAuthenticationManager
import com.mohaeng.infrastructure.authentication.oauth2.handler.OAuth2AuthenticationFailureHandler
import com.mohaeng.infrastructure.authentication.oauth2.handler.OAuth2AuthenticationSuccessHandler
import com.mohaeng.infrastructure.authentication.oauth2.repo.OAuth2AuthorizationRequestBasedOnSessionRepository
import com.mohaeng.infrastructure.authentication.oauth2.userservice.OAuth2SignUpLoginMemberService
import com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.OAuth2AttributesToMemberConverterProvider
import com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter.GoogleToMemberConverter
import com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter.KakaoToMemberConverter
import com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter.NaverToMemberConverter
import com.mohaeng.infrastructure.authentication.oauth2.userservice.usecase.OAuth2SignUpUseCase
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

            //== URL ?????? ?????? ?????? ==//
            authorizeHttpRequests {
                // set permit all uri
                PermitAllURI.values().map { value ->
                    when (value.method) {
                        //method??? null??? ?????? -> ?????? ???????????? ?????? ??????
                        null -> authorize(AntPathRequestMatcher(value.uri), permitAll)
                        else -> authorize(AntPathRequestMatcher(value.uri, value.method.name), permitAll)
                    }
                }

                authorize(anyRequest, authenticated)
            }

            //== csrf ?????? X ==//
            csrf { disable() }

            //== httpBasic ?????? X ==//
            httpBasic { disable() }

            //== session ?????? X ==//
            sessionManagement { sessionCreationPolicy = STATELESS }

            // h2 ????????? ??? ????????? ??????
            headers { frameOptions { sameOrigin = true } }

            // OAuth2 ????????? ??????
            oauth2Login {
                authorizationEndpoint {
                    /*
                     http://~~~~/{baseUri} ??? ????????? ???????????? OAuth2 ????????? ????????????
                     EX : http://localhost:9999/login/oauth2/authorization/kakao
                     */
                    baseUri = "/login/oauth2/authorization"
                    authorizationRequestRepository = OAuth2AuthorizationRequestBasedOnSessionRepository()
                }

                redirectionEndpoint {
                    // ????????? API ?????? ??? Redirect URL ????????? ???????????? ???
                    baseUri = "/*/oauth2/code/*"
                }

                // OAuth2 ?????? ?????? ??? ????????? ????????? ????????? ???????????? ?????????
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
     * OAuth2 ????????? ????????? ?????? ?????? ??????
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
     * OAuth2 ??????????????? ?????? ????????? ????????? Member??? ???????????? Converter
     * OAuth2SignUpLoginUserService?????? ??????
     *
     * OAuth2 ???????????? ???????????? ??? ????????? ??? ??????
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
     * OAuth2 ????????? ?????? ??? ?????????
     */
    @Bean
    fun oauth2AuthenticationSuccessHandler(
        authTokenCreateUseCase: AuthTokenCreateUseCase? = null,
    ): OAuth2AuthenticationSuccessHandler {

        checkNotNull(authTokenCreateUseCase) { "authTokenCreateUseCase is Null" }
        return OAuth2AuthenticationSuccessHandler(authTokenCreateUseCase)
    }



    /**
     * OAuth2 ????????? ?????? ??? ?????????
     */
    @Bean
    fun oauth2AuthenticationFailureHandler(): OAuth2AuthenticationFailureHandler {
        return OAuth2AuthenticationFailureHandler()
    }



    /**
     * Json?????? ????????? ???????????? ??????
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
     * Json?????? ????????? ?????? ??? ????????? ???????????? ??????
     */
    @Bean
    fun jsonAuthenticationProvider(
        encoder: MemberPasswordEncoder,
        loginUserService: LoadMemberUseCase,
    ): JsonAuthenticationProvider = JsonAuthenticationProvider(encoder, loginUserService)



    /**
     * Json?????? ????????? ?????? ??? ??????
     */
    @Bean
    fun jsonAuthenticationSuccessHandler(
        authTokenCreateUseCase: AuthTokenCreateUseCase? = null,
    ): JsonAuthenticationSuccessHandler {

        checkNotNull(authTokenCreateUseCase) { "authTokenCreateUseCase is Null" }
        return JsonAuthenticationSuccessHandler(authTokenCreateUseCase)
    }


    /**
     * Json?????? ????????? ?????? ??? ??????
     */
    @Bean
    fun jsonAuthenticationFailureHandler(
        objectMapper: ObjectMapper? = null
    ): JsonAuthenticationFailureHandler {

        checkNotNull(objectMapper) { "objectMapper is Null" }
        return JsonAuthenticationFailureHandler(objectMapper)
    }



    /**
     * JWT??? ?????? ???????????? ??????
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
     * JWT ?????? ?????? ?????? ????????? ?????? ????????? ???????????? ?????????
     */
    @Bean
    fun jwtAuthenticationManager(
        extractAuthMemberFromAuthTokenUseCase: ExtractAuthMemberFromAuthTokenUseCase
    ): JwtAuthenticationManager {

        checkNotNull(extractAuthMemberFromAuthTokenUseCase) { "extractAuthMemberFromAuthTokenUseCase is Null" }
        return JwtAuthenticationManager(extractAuthMemberFromAuthTokenUseCase)
    }


    /**
     * JWT ?????? ?????? ??? ??????
     */
    @Bean
    fun jwtAuthenticationFailureHandler(
        objectMapper: ObjectMapper? = null
    ): JwtAuthenticationFailureHandler {

        checkNotNull(objectMapper) { "objectMapper is Null" }
        return JwtAuthenticationFailureHandler(objectMapper)
    }





    /**
     * ?????? ?????? ??? ??????
     */
    @Bean
    fun sendErrorAccessDeniedHandler(
        objectMapper: ObjectMapper? = null,
    ): SendErrorAccessDeniedHandler {

        checkNotNull(objectMapper) { "objectMapper is null" }

        return SendErrorAccessDeniedHandler(objectMapper)
    }

    /**
     * ?????? ?????? ??? ??????
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
     * WebSecurity - ??????,?????? ?????? ?????? X
     * HttpSecurity - antMatchers??? ?????? endpoint??? ?????? '??????'??? ????????????.
     */
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer
            = WebSecurityCustomizer { it.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**").mvcMatchers("/favicon.ico") }


}