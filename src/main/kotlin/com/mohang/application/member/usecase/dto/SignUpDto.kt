package com.mohang.application.member.usecase.dto

import com.mohang.domain.enums.Role.BASIC
import com.mohang.domain.enums.OAuth2Type.NONE
import com.mohang.domain.member.Member
import com.mohang.domain.member.OAuth2LoginId

/**
 * Created by ShinD on 2022/08/30.
 */
data class SignUpDto(

    val email: String,  // 이메일

    val password: String, // 비밀번호

    val name: String, // 이름

    val nickname: String, // 닉네임

    val profileImagePath: String?, // 프로필 사진 경로

    val username: String, // 아이디
) {

    fun toEntity() =
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