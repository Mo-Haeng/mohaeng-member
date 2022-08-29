package com.mohang.application.usecase.signup

import com.mohang.application.usecase.signup.dto.NaverSignUpDto
import com.mohang.infrastructure.persistence.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

/**
 * Created by ShinD on 2022/08/30.
 */
@Service
class NaverSignUpUseCase (

    private val memberRepository: MemberRepository,

    private val transaction: TransactionTemplate,

    ) {

    /**
     * 네이버 회원 가입
     *
     * 중복 확인 후 등록
     */
    fun command(sud: NaverSignUpDto): Long {
        TODO("NOT Implement")
    }
}