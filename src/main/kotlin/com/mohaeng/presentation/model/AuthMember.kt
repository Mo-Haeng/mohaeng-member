package com.mohaeng.presentation.model

import com.mohaeng.domain.enums.Role

/**
 * Created by ShinD on 2022/09/06.
 *
 * Presentation layer에서 사용할 회원 인증 정보
 */
data class AuthMember(

    val id: Long,

    val role: Role,
)