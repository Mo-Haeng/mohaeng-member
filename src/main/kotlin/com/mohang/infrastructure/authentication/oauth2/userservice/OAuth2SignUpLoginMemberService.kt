package com.mohang.infrastructure.authentication.oauth2.userservice

import com.mohang.domain.enums.OAuth2Type
import com.mohang.infrastructure.authentication.oauth2.userservice.provider.OAuth2AttributesToMemberConverterProvider
import com.mohang.infrastructure.authentication.oauth2.userservice.usecase.OAuth2SignUpUseCase
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService
import org.springframework.security.oauth2.core.user.OAuth2User

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2SignUpLoginMemberService(

    private val signUpUseCase: OAuth2SignUpUseCase,

    private val provider: OAuth2AttributesToMemberConverterProvider,

    private val delegate: OAuth2UserService<OAuth2UserRequest, OAuth2User> = DefaultOAuth2UserService(),
) : OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    /**
     * OAuth2 로그인 제공 서비스에 요청해서 사용자 정보 조회하여 반환해주는 메서드
     */
    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {

        // DefaultOAuth2UserService 에게 위임
        val oauth2User = delegate.loadUser(userRequest)

        // registrationId = kakao, google 등등
        val oauth2Type = OAuth2Type.fromRegistrationId(userRequest.clientRegistration.registrationId)

        // OAuth 타입과, attributes를 통해 Member로 변환
        val member = provider.convert(oauth2Type, oauth2User.attributes)

        // 회원가입되지 않은 회원인 경우 회원가입
        val savedMember = signUpUseCase.command(member)

        return AuthMemberPrinciple(id = savedMember.id!!, role = savedMember.role, attributes = oauth2User.attributes, oauth2LoginId = member.oauth2LoginId)
    }
}