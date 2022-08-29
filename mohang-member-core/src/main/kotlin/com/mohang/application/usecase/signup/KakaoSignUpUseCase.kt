package com.mohang.application.usecase.signup

import com.mohang.application.usecase.signup.dto.KakaoSignUpDto
import com.mohang.infrastructure.persistence.MemberRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

/**
 * Created by ShinD on 2022/08/30.
 */
@Service
class KakaoSignUpUseCase (

    private val memberRepository: MemberRepository,

    private val transaction: TransactionTemplate,

    ) {

    /**
     * 카카오톡 회원 가입
     *
     * 중복 확인 후 등록
     */
    fun command(sud: KakaoSignUpDto): Long {
        TODO("NOT Implement")
    }
}