package com.mohang.application.usecase.signup.dto

import com.mohang.domain.Member
import com.mohang.domain.RegistrationId
import com.mohang.domain.enums.RegistrationType.GENERAL
import com.mohang.domain.enums.Role.BASIC

/**
 * Created by ShinD on 2022/08/30.
 */
data class GeneralSignUpDto(

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
            registrationId = RegistrationId(registrationType = GENERAL, registrationIdValue = username),
        )
}