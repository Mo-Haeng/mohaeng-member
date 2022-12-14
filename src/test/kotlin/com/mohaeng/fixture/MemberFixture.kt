package com.mohaeng.fixture

import com.mohaeng.application.member.usecase.dto.SignUpDto
import com.mohaeng.domain.enums.OAuth2Type.NONE
import com.mohaeng.domain.enums.Role
import com.mohaeng.domain.member.Member
import com.mohaeng.domain.member.OAuth2LoginId
import com.mohaeng.infrastructure.authentication.principle.AuthMemberPrinciple
import com.mohaeng.presentation.model.SignUpRequest
import com.mohaeng.query.data.MemberData
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
    var OAUTH2_LOGIN_ID: OAuth2LoginId = OAuth2LoginId(oauth2Type = NONE, value = USERNAME)

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
        oauth2LoginId: OAuth2LoginId = OAUTH2_LOGIN_ID

    ) =
        Member(
            role = role,
            email = email,
            password = password,
            nickname = nickname,
            name = name,
            point = point,
            profileImagePath = profileImagePath,
            oauth2LoginId = oauth2LoginId,
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
        oauth2LoginId: OAuth2LoginId = OAUTH2_LOGIN_ID

    ): Member {
        val member = Member(
            role = role,
            email = email,
            password = password,
            nickname = nickname,
            name = name,
            point = point,
            profileImagePath = profileImagePath,
            oauth2LoginId = oauth2LoginId,
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

    fun authBasicMemberPrinciple(
        id: Long = ID,
        username: String = USERNAME,
        password: String = PASSWORD,
        role: Role = ROLE
    ) =
        AuthMemberPrinciple(
            id = id,
            oauth2LoginId  = OAuth2LoginId(NONE, username),
            password = password,
            role = role,
        )

    fun memberData(

        id: Long = ID,
        createdAt: LocalDateTime = CREATED_AT,
        modifiedAt: LocalDateTime = MODIFIED_AT,
        username: String? = USERNAME,
        role: Role = ROLE,
        email: String = EMAIL,
        password: String = PASSWORD,
        name: String = NAME,
        nickname: String = NICKNAME,
        point: Int = POINT,
        profileImagePath: String = PROFILE_IMAGE_PATH,
        oauth2LoginId: OAuth2LoginId = OAUTH2_LOGIN_ID

    ): MemberData {
        return MemberData(
            id = id,
            role = role,
            oauth2Type = oauth2LoginId.oauth2Type,
            username = username,
            name = name,
            nickname = nickname,
            email = email,
            profileImagePath = profileImagePath,
            point = point,
            createdAt = CREATED_AT.toString(),
            modifiedAt = MODIFIED_AT.toString(),
        )
    }
}