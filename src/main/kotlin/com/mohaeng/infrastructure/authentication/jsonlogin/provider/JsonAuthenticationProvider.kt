package com.mohaeng.infrastructure.authentication.jsonlogin.provider

import com.mohaeng.domain.enums.OAuth2Type.NONE
import com.mohaeng.domain.member.MemberPasswordEncoder
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.infrastructure.authentication.jsonlogin.authentication.JsonUsernamePasswordToken
import com.mohaeng.infrastructure.authentication.jsonlogin.provider.usecase.LoadMemberUseCase
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple
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
     * JsonUsernamePasswordToken에 담긴 username과 password를 사용하여 사용자 인증 정보(Authentication)를 반환
     */
    override fun authenticate(authentication: Authentication): Authentication {

        // username과 password 가져오기
        val username = authentication.principal as String
        val password = authentication.credentials as String

        // 기본 로그인 식별값 만들기
        val oAuth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username)

        // 일치하는 회원 찾아오기 -> 없다면 오류 발생
        val authMemberPrinciple: AuthMemberPrinciple = loadMemberUseCase.command(oAuth2LoginId)

        // 비밀번호 일치 여부 검사
        checkPassword(password, authMemberPrinciple)

        // principle 로 사용할 사용자 정보 생성 (비밀번호는 숨김)
        val authMember =
            AuthMemberPrinciple(id = authMemberPrinciple.id, oauth2LoginId = oAuth2LoginId, role = authMemberPrinciple.role)

        // 인증정보 반환
        return JsonUsernamePasswordToken(authMember, authMemberPrinciple.password, authMember.role)
    }

    /**
     *  비밀번호 일치여부 검사
     */
    private fun checkPassword(password: String, authMemberPrinciple: AuthMemberPrinciple) {

        if (authMemberPrinciple.password == null) {
            throw BadCredentialsException("비밀번호가 잘못되었습니다")
        }

        if (!encoder.matches(password, authMemberPrinciple.password!!))
            throw BadCredentialsException("비밀번호가 잘못되었습니다")
    }

    /**
     * JsonUsernamePasswordToken 허용
     */
    override fun supports(authentication: Class<*>): Boolean {
        return authentication == JsonUsernamePasswordToken::class.java
    }
}