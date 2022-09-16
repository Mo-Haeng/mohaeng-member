package com.mohaeng.query.data

import com.mohaeng.domain.enums.OAuth2Type
import com.mohaeng.domain.enums.Role
import com.querydsl.core.annotations.QueryProjection

/**
 * Created by ShinD on 2022/09/06.
 */
data class MemberData @QueryProjection constructor(

    val id: Long, // PK

    var role: Role, // 권한

    val oauth2Type: OAuth2Type, // 회원가입 타입 - NONE(일반 회원가입), NAVER, GOOGLE, KAKAO

    val username: String? = null, // oauth2Type = NONE 인 경우에만 존재

    var name: String, // 이름

    var nickname: String, // 닉네임

    var email: String? = null, // 이메일

    var profileImagePath: String?, // 프로필 사진 경로 (https://~~)

    var point: Int = 0, // 포인트

    var createdAt: String, // 생성 시간

    var modifiedAt: String, // 최종 수정 시간
)