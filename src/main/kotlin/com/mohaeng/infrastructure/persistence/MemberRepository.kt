package com.mohaeng.infrastructure.persistence

import com.mohaeng.domain.member.Member
import com.mohaeng.domain.member.OAuth2LoginId
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Created by ShinD on 2022/08/30.
 */
interface MemberRepository : JpaRepository<Member, Long> {

    /**
     * 회원가입 시 중복 검사를 위해 사용된다.
     */
    fun findByOauth2LoginId(oauth2LoginId: OAuth2LoginId): Member?

    fun findByEmail(email: String): Member?
}