package com.mohang.infrastructure.authentication.jsonlogin.provider.usecase

import com.mohang.domain.member.OAuth2LoginId
import com.mohang.infrastructure.authentication.jsonlogin.userdetails.MemberDetails
import com.mohang.infrastructure.persistence.MemberRepository
import org.springframework.stereotype.Service
import javax.security.auth.message.AuthException

/**
 * Created by ShinD on 2022/09/01.
 *
 * Json 로그인 시 사용되며, JsonAuthenticationProvider에서 호출한다.
 */
@Service
class LoadMemberUseCase(

    private val memberRepository: MemberRepository,

) {

    fun command(oAuth2LoginId: OAuth2LoginId): MemberDetails {
        // 회원 정보가 존재하지 않는 경우 예외 발생
        val member = memberRepository.findByOauth2LoginId(oAuth2LoginId)
            ?: throw AuthException("일치하는 회원이 없습니다.")

        return MemberDetails(
            id = member.id!!,
            username = oAuth2LoginId.value,
            password = member.password!!,
            role = member.role
        )
    }
}