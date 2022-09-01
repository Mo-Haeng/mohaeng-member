package com.mohang.application.usecase.dto

import com.mohang.domain.enums.Role.BASIC
import com.mohang.domain.enums.OAuth2Type.NONE
import com.mohang.domain.member.Member
import com.mohang.domain.member.OAuth2LoginId

/**
 * Created by ShinD on 2022/08/30.
 */
interface SignUpDto {

    fun toEntity(): Member
}



/**
 * 일반 회원가입시 사용
 */
data class BasicSignUpDto(

    val email: String,  // 이메일

    val password: String, // 비밀번호

    val name: String, // 이름

    val nickname: String, // 닉네임

    val profileImagePath: String?, // 프로필 사진 경로

    val username: String, // 아이디

) : SignUpDto {

    override fun toEntity() =
        Member(
            role = BASIC,
            email = email,
            password = password,
            name = name,
            nickname = nickname,
            profileImagePath = profileImagePath,
            oauth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = username),
        )
}