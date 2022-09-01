package com.mohang.infrastructure.auth.jsonlogin.userdetails

import com.mohang.domain.enums.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User

/**
 * Created by ShinD on 2022/09/01.
 */
class MemberDetails(

    val id: Long,
    username: String,
    password: String,
    val role: Role,
) : User(
    username,
    password,
    listOf(SimpleGrantedAuthority(role.authority)) //SimpleGrantedAuthority(ROLE_XXX)) 이런 식으로 사용함
)