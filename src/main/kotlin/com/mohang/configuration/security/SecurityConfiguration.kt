package com.mohang.configuration.security

import com.mohang.application.usecase.OAuth2SignUpUseCase
import com.mohang.configuration.security.enums.PermitAllURI
import com.mohang.domain.enums.Role.BASIC
import com.mohang.infrastructure.auth.oauth2.OAuth2SignUpLoginUserService
import com.mohang.infrastructure.auth.oauth2.handler.OAuth2AuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.web.SecurityFilterChain

/**
 * Created by ShinD on 2022/08/31.
 */
@Configuration
class SecurityConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http {

            authorizeRequests {
                // set permit all uri
                PermitAllURI.values().map { value ->
                    when (value.method) {
                        //method가 null인 경우 -> 모든 메서드에 대해 허용
                        null -> authorize(pattern = value.uri, permitAll)
                        else -> authorize(method = value.method, pattern = value.uri, permitAll)
                    }
                }
                // 이외 모든 요청은 BASIC 이상이어야 함
                authorize(anyRequest, hasRole(BASIC.name))
            }

            csrf { disable() }
            httpBasic { disable() }
            sessionManagement { sessionCreationPolicy = STATELESS }

            // h2 사용할 수 있도록 설정
            headers { frameOptions { sameOrigin } }

            // OAuth2 로그인 사용
            oauth2Login {

                authorizationEndpoint {
                    /*
                     http://~~~~/{baseUri} 로 요청이 들어오면 OAuth2 인증을 수행한다
                     EX : http://localhost:9999/login/oauth2/authorization/kakao
                     */
                    baseUri = "/login/oauth2/authorization"
                }

                redirectionEndpoint {
                    // 로그인 API 등록 시 Redirect URL 설정과 동일해야 함
                    baseUri = "/*/oauth2/code/*"
                }

                // OAuth2 인증 성공 시 사용자 정보를 받아와 파싱하는 서비스
                userInfoEndpoint {
                    userService = oauth2SignUpLoginUserService()
                }

                authenticationSuccessHandler = OAuth2AuthenticationSuccessHandler()

                // authenticationFailureHandler = 인증 실패 시 처리할 핸들러 TODO(" 적절한 예외 메세지 ")
            }

            // addFilterBefore<>() TODO(" JSON 로그인 구현 ")
        }

        return http.build()
    }



    /**
     * OAuth2 로그인 사용자 정보 저장 관리
     */
    @Bean
    fun oauth2SignUpLoginUserService(
        oauth2SignUpUseCase: OAuth2SignUpUseCase? = null
    ): OAuth2SignUpLoginUserService {

        checkNotNull(oauth2SignUpUseCase) { "oAuthSignUpUseCase is Null" }
        return OAuth2SignUpLoginUserService(oauth2SignUpUseCase)
    }

    /**
     * OAuth2 로그인 사용자 정보 저장 관리
     */
    @Bean
    fun oauth2AuthenticationSuccessHandler(): OAuth2AuthenticationSuccessHandler {
        return OAuth2AuthenticationSuccessHandler()
    }
}