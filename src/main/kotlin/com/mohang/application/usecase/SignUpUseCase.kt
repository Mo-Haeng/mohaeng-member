package com.mohang.application.usecase

import com.mohang.application.exception.DuplicateUsernameException
import com.mohang.application.usecase.dto.SignUpDto
import com.mohang.domain.member.Member
import com.mohang.domain.member.MemberPasswordEncoder
import com.mohang.infrastructure.persistence.MemberRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

/**
 * Created by ShinD on 2022/08/30.
 */
@Service
class SignUpUseCase<in T : SignUpDto> (

    private val memberRepository: MemberRepository,

    private val transaction: TransactionTemplate,

    private val passwordEncoder: MemberPasswordEncoder,

) {

    private val log = KotlinLogging.logger {  }

    /**
     * 회원 가입
     */
    fun command(signUpDto: T): Long {

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

        } !! // Long? 타입이 반환되므로 강제 not null 처리
    }


    /**
     * 중복 가입 정보 체크
     */
    private fun checkDuplicate(member: Member) {

        memberRepository.findBySocialLoginId(member.socialLoginId)
            //이미 존재하는 경우 Exception 발생
            ?.let { throw DuplicateUsernameException() }
    }
}