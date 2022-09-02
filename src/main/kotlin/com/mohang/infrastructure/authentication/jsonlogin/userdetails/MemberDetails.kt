package com.mohang.infrastructure.authentication.jsonlogin.userdetails

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
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MemberDetails) return false
        if (!super.equals(other)) return false

        if (id != other.id) return false
        if (role != other.role) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + role.hashCode()
        return result
    }
}