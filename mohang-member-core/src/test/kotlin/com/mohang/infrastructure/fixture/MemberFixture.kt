package com.mohang.infrastructure.fixture

import com.mohang.domain.Member
import com.mohang.domain.RegistrationId
import com.mohang.domain.enums.RegistrationType
import com.mohang.domain.enums.Role

/**
 * Created by ShinD on 2022/08/30.
 */
object MemberFixture {

    val ROLE = Role.BASIC

    const val EMAIL = "sample@email.com"

    const val PASSWORD = "samplePassword"

    const val NICKNAME = "sample nickname"

    const val POINT = 100

    const val PROFILE_IMAGE_PATH = "http://sample.url.com"


    const val USERNAME = "sample username"
    var REGISTRATION_ID: RegistrationId = RegistrationId(registrationType = RegistrationType.GENERAL, registrationIdValue = USERNAME)

    const val KAKAO_ID = "222222"
    const val NAVER_ID = "111111"



    /**
     * 아직 가입하지 않은 회원
     */
    fun notSavedMember(

        role: Role = ROLE,
        email: String = EMAIL,
        password: String = PASSWORD,
        nickname: String = NICKNAME,
        point: Int = POINT,
        profileImagePath: String = PROFILE_IMAGE_PATH,
        registrationId: RegistrationId = REGISTRATION_ID

    ) =
        Member(
            role = role,
            email = email,
            password = password,
            nickname = nickname,
            point = point,
            profileImagePath = profileImagePath,
            registrationId = registrationId,
        )
}