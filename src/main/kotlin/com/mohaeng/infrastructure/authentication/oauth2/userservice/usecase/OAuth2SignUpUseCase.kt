package com.mohaeng.infrastructure.authentication.oauth2.userservice.usecase

import com.mohaeng.application.member.exception.DuplicateEmailException
import com.mohaeng.domain.member.Member
import com.mohaeng.infrastructure.persistence.MemberRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

/**
 * Created by ShinD on 2022/09/01.
 *
 * OAuth2 로그인 시 사용되며, OAuth2SignUpLoginUserService에서 사용한다.
 */
@Service
class OAuth2SignUpUseCase(

    private val memberRepository: MemberRepository,

    private val transaction: TransactionTemplate,
) {

    private val log = KotlinLogging.logger { }

    /**
     * 회원 가입
     */
    fun command(member: Member): Member {

        log.debug { "OAuthSignUpUseCase.command()" }

        return memberRepository.findByOauth2LoginId(member.oauth2LoginId)
        // 존재하지 않는 경우 회원가입 진행
            ?: signUp(member)
    }

    private fun signUp(member: Member) =

        transaction.execute {
            // 이메일 중복 검사
            checkDuplicateEmail(member)

            memberRepository.save(member)
        }!!

    private fun checkDuplicateEmail(member: Member) {
        memberRepository.findByEmail(member.email!!)?.let {
            throw DuplicateEmailException()
        }
    }
}