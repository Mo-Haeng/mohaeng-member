package com.mohang.fixture

import com.mohang.application.usecase.signup.dto.GeneralSignUpDto
import com.mohang.domain.Member
import com.mohang.domain.RegistrationId
import com.mohang.domain.enums.RegistrationType
import com.mohang.domain.enums.Role
import org.springframework.test.util.ReflectionTestUtils
import java.time.LocalDateTime

/**
 * Created by ShinD on 2022/08/30.
 */
object MemberFixture {

    const val ID = 10L
    val CREATED_AT = LocalDateTime.now()
    val MODIFIED_AT = LocalDateTime.now()

    val ROLE = Role.BASIC

    const val EMAIL = "sample@email.com"

    const val PASSWORD = "samplePassword"

    const val NICKNAME = "sample nickname"

    const val NAME = "sample name"

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
        name: String = NAME,
        point: Int = POINT,
        profileImagePath: String = PROFILE_IMAGE_PATH,
        registrationId: RegistrationId = REGISTRATION_ID

    ) =
        Member(
            role = role,
            email = email,
            password = password,
            nickname = nickname,
            name = name,
            point = point,
            profileImagePath = profileImagePath,
            registrationId = registrationId,
        )

    /**
     * 저장된 회원
     */
    fun savedMember(

        id: Long = ID,
        createdAt: LocalDateTime = CREATED_AT,
        modifiedAt: LocalDateTime = MODIFIED_AT,
        role: Role = ROLE,
        email: String = EMAIL,
        password: String = PASSWORD,
        name: String = NAME,
        nickname: String = NICKNAME,
        point: Int = POINT,
        profileImagePath: String = PROFILE_IMAGE_PATH,
        registrationId: RegistrationId = REGISTRATION_ID

    ): Member {
        val member = Member(
            role = role,
            email = email,
            password = password,
            name = name,
            nickname = nickname,
            point = point,
            profileImagePath = profileImagePath,
            registrationId = registrationId,
        )

        ReflectionTestUtils.setField(member, "id", id)
        ReflectionTestUtils.setField(member, "createdAt", createdAt)
        ReflectionTestUtils.setField(member, "modifiedAt", modifiedAt)

        return member
    }


    fun generalSignUpDto(

        email: String = EMAIL,
        password: String = PASSWORD,
        nickname: String = NICKNAME,
        name: String = NAME,
        profileImagePath: String = PROFILE_IMAGE_PATH,
        username: String = USERNAME,

    ) =
        GeneralSignUpDto(
            email = email,
            password = password,
            nickname = nickname,
            name = name,
            profileImagePath = profileImagePath,
            username = username
        )

}