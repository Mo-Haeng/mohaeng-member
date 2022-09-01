package com.mohang.infrastructure.auth.jsonlogin.principle

import com.mohang.domain.enums.Role

/**
 * Created by ShinD on 2022/09/01.
 */
class AuthMember(

    val id: Long,
    val username: String,
    val role: Role,
)