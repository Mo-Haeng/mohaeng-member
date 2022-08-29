package com.mohang.infrastructure.persistence

import com.mohang.domain.Member
import com.mohang.domain.RegistrationId
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by ShinD on 2022/08/30.
 */
interface MemberRepository : JpaRepository<Member, Long> {

    /**
     * 회원 가입 시 중복 가입을 방지하기 위해 사용한다.
     */
    fun findByRegistrationId(registrationId: RegistrationId): Member?
}