package com.mohang.infrastructure.authentication.jsonlogin.userdetails

import com.mohang.domain.enums.Role
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull

/**
 * Created by ShinD on 2022/09/02.
 */
internal class MemberDetailsTest {

    @Test
    fun `MemberDetails 정보 테스트`() {

        //given
        val memberId = 1L
        val username = "username"
        val password = "password"
        val role = Role.BASIC

        //when
        val memberDetails = MemberDetails(
            id = memberId,
            username = username,
            password = password,
            role = role,
        )

        //then
        expectThat(memberDetails.id) { isEqualTo(memberId) }
        expectThat(memberDetails.username) { isEqualTo(username) }
        expectThat(memberDetails.password) { isEqualTo(password) }
        expectThat(memberDetails.role) { isEqualTo(role) }
    }

    @Test
    fun `eraseCredentials 테스트`() {

        //given
        val memberId = 1L
        val username = "username"
        val password = "password"
        val role = Role.BASIC
        val memberDetails = MemberDetails(
            id = memberId,
            username = username,
            password = password,
            role = role,
        )

        //when
        memberDetails.eraseCredentials()

        //then
        expectThat(memberDetails.password) { isNull() }
    }
}