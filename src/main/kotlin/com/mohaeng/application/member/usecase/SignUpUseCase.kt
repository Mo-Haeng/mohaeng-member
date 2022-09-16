package com.mohaeng.application.member.usecase

import com.mohaeng.application.member.exception.DuplicateEmailException
import com.mohaeng.application.member.exception.DuplicateUsernameException
import com.mohaeng.application.member.usecase.dto.SignUpDto
import com.mohaeng.domain.member.Member
import com.mohaeng.domain.member.MemberPasswordEncoder
import com.mohaeng.infrastructure.persistence.MemberRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

/**
 * Created by ShinD on 2022/08/30.
 */
@Service
class SignUpUseCase(

    private val memberRepository: MemberRepository,

    private val transaction: TransactionTemplate,

    private val passwordEncoder: MemberPasswordEncoder,
) {

    private val log = KotlinLogging.logger { }

    /**
     * 회원 가입
     */
    fun command(signUpDto: SignUpDto): Long {

        log.debug { "SignUpUseCase.command()" }

        val member = signUpDto.toEntity()

        // 계정정보 중복 검사
        checkDuplicate(member)

        // 비밀번호 암호화
        member.passwordEncoding(passwordEncoder)

        // 트랜잭션 시작
        return transaction.execute {

            //회원정보 저장
            memberRepository.save(member).id

        }!! // Long? 타입이 반환되므로 강제 not null 처리
    }

    /**
     * 중복 가입 정보 체크
     */
    private fun checkDuplicate(member: Member) {
        with(member) {
            // 이메일 중복 검사
            memberRepository.findByEmail(email!!)?.let {
                throw DuplicateEmailException()
            }

            // 아이디 중복 검사
            memberRepository.findByOauth2LoginId(oauth2LoginId)?.let {
                throw DuplicateUsernameException()
            }
        }
    }
}