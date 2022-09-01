package com.mohang.infrastructure.auth.oauth2.userservice

import com.mohang.domain.enums.OAuth2Type
import com.mohang.infrastructure.auth.oauth2.principle.OAuth2Member
import com.mohang.infrastructure.auth.oauth2.userservice.converter.OAuth2AttributesToMemberConverter
import com.mohang.infrastructure.auth.oauth2.userservice.usecase.OAuth2SignUpUseCase
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2SignUpLoginUserService(

    private val signUpUseCase: OAuth2SignUpUseCase,

    ) : DefaultOAuth2UserService(){

    private val converter: OAuth2AttributesToMemberConverter = OAuth2AttributesToMemberConverter()

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {

        val oauth2User = super.loadUser(userRequest)

        // registrationId = kakao, google 등등
        val oauth2Type = OAuth2Type.fromRegistrationId(userRequest.clientRegistration.registrationId)

        // OAuth 타입과, attributes를 통해 Member로 변환
        val member = converter.convert(oauth2Type, oauth2User.attributes)

        // 회원가입되지 않은 회원인 경우 회원가입
        val savedMember = signUpUseCase.command(member)

        return OAuth2Member(savedMember.id!!, savedMember.role, oauth2User.attributes, oauth2Type.oauth2IdName)
    }
}