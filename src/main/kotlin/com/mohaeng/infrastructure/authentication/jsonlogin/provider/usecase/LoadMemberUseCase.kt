package com.mohaeng.infrastructure.authentication.jsonlogin.provider.usecase

import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple
import com.mohaeng.infrastructure.persistence.MemberRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Created by ShinD on 2022/09/01.
 *
 * Json 로그인 시 사용되며, JsonAuthenticationProvider에서 호출한다.
 */
@Service
class LoadMemberUseCase(

    private val memberRepository: MemberRepository,

) {

    fun command(oAuth2LoginId: OAuth2LoginId): AuthMemberPrinciple {
        // 회원 정보가 존재하지 않는 경우 예외 발생
        val member = memberRepository.findByOauth2LoginId(oAuth2LoginId)
            ?: throw UsernameNotFoundException("일치하는 회원이 없습니다.")

        return AuthMemberPrinciple (
            id = member.id!!,
            oauth2LoginId = oAuth2LoginId,
            password = member.password!!,
            role = member.role
        )
    }
}