package com.mohang.domain

/**
 * Created by ShinD on 2022/08/30.
 */
interface MemberPasswordEncoder {

    /**
     * 비밀번호 인코딩
     */
    fun encode(rawPassword: String): String

    /**
     * 비밀번호 일치여부 확인
     */
    fun matches(rawPassword: String, encodedPassword: String): Boolean
}