package com.mohang.fixture

import com.mohang.application.member.usecase.dto.SignUpDto
import com.mohang.domain.enums.OAuth2Type
import com.mohang.domain.enums.Role
import com.mohang.domain.member.Member
import com.mohang.domain.member.OAuth2LoginId
import com.mohang.presentation.model.SignUpRequest
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
    var SOCIAL_LOGIN_ID: OAuth2LoginId = OAuth2LoginId(oauth2Type = OAuth2Type.NONE, value = USERNAME)

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
        socialLoginId: OAuth2LoginId = SOCIAL_LOGIN_ID

    ) =
        Member(
            role = role,
            email = email,
            password = password,
            nickname = nickname,
            name = name,
            point = point,
            profileImagePath = profileImagePath,
            oauth2LoginId = socialLoginId,
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
        socialLoginId: OAuth2LoginId = SOCIAL_LOGIN_ID

    ): Member {
        val member = Member(
            role = role,
            email = email,
            password = password,
            nickname = nickname,
            name = name,
            point = point,
            profileImagePath = profileImagePath,
            oauth2LoginId = socialLoginId,
        )

        ReflectionTestUtils.setField(member, "id", id)
        ReflectionTestUtils.setField(member, "createdAt", createdAt)
        ReflectionTestUtils.setField(member, "modifiedAt", modifiedAt)

        return member
    }


    fun basicSignUpDto(

        email: String = EMAIL,
        password: String = PASSWORD,
        nickname: String = NICKNAME,
        name: String = NAME,
        profileImagePath: String = PROFILE_IMAGE_PATH,
        username: String = USERNAME,

        ) =
        SignUpDto(
            email = email,
            password = password,
            nickname = nickname,
            name = name,
            profileImagePath = profileImagePath,
            username = username
        )

    fun signUpRequest(

        email: String = EMAIL,
        password: String = PASSWORD,
        nickname: String = NICKNAME,
        name: String = NAME,
        profileImagePath: String = PROFILE_IMAGE_PATH,
        username: String = USERNAME,

    ) =
        SignUpRequest(
            email = email,
            username = username,
            name = name,
            password = password,
            nickname = nickname,
            profileImagePath = profileImagePath,
        )

}