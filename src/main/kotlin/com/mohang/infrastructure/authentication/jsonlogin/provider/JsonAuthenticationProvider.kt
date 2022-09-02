package com.mohang.infrastructure.authentication.jsonlogin.provider

import com.mohang.domain.enums.OAuth2Type.NONE
import com.mohang.domain.member.MemberPasswordEncoder
import com.mohang.domain.member.OAuth2LoginId
import com.mohang.infrastructure.authentication.jsonlogin.authentication.JsonUsernamePasswordToken
import com.mohang.infrastructure.authentication.jsonlogin.provider.usecase.LoadMemberUseCase
import com.mohang.infrastructure.authentication.jsonlogin.userdetails.MemberDetails
import com.mohang.infrastructure.authentication.principle.AuthMemberPrinciple
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication

/**
 * Created by ShinD on 2022/09/01.
 */
class JsonAuthenticationProvider(

    private val encoder: MemberPasswordEncoder,

    private val loadMemberUseCase: LoadMemberUseCase,

) : AuthenticationProvider {

    /**
     * 인증 수행 -> 반환 결과가
     */
    override fun authenticate(authentication: Authentication): Authentication {

        // username과 password 가져오기
        val username = authentication.principal as String
        val password = authentication.credentials as String

        // 기본 로그인 식별값 만들기
        val oAuth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username)

        // 일치하는 회원 찾아오기 -> 없다면 오류 발생
        val memberDetails: MemberDetails = loadMemberUseCase.command(oAuth2LoginId)

        // 비밀번호 일치 여부 검사
        checkPassword(password, memberDetails)

        // principle 로 사용할 사용자 정보 생성

        val authMember =
            AuthMemberPrinciple(id = memberDetails.id, oauth2LoginId = oAuth2LoginId, role = memberDetails.role)

        // 인증정보 반환
        return JsonUsernamePasswordToken(authMember, memberDetails.password, authMember.role)
    }

    /**
     *  비밀번호 일치여부 검사
     */
    private fun checkPassword(password: String, memberDetails: MemberDetails) {
        if (!encoder.matches(password, memberDetails.password))
            throw BadCredentialsException("비밀번호가 잘못되었습니다")
    }

    /**
     * JsonUsernamePasswordToken 허용
     */
    override fun supports(authentication: Class<*>): Boolean {
        return authentication == JsonUsernamePasswordToken::class.java
    }
}