package com.mohang.infrastructure.auth.principle

import com.mohang.domain.enums.Role
import com.mohang.domain.enums.Role.Companion.isBlack
import com.mohang.domain.member.OAuth2LoginId
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.oauth2.core.user.OAuth2User

/**
 * Created by ShinD on 2022/09/02.
 */
class AuthMemberPrinciple (

    val id: Long,

    private val password: String? = null,

    val oauth2LoginId: OAuth2LoginId,

    val role: Role,

    private val attributes: MutableMap<String, Any> = mutableMapOf(),

    ) : OAuth2User, UserDetails {

    // AuthenticatedPrincipal
    override fun getName() = id.toString()

    // AuthenticatedPrincipal
    override fun getAttributes() = attributes


    // UserDetails
    override fun getAuthorities() = setOf(SimpleGrantedAuthority(role.authority))

    // UserDetails
    override fun getPassword() = password


    // UserDetails
    override fun getUsername() = oauth2LoginId.value


    // UserDetails
    override fun isAccountNonExpired() = !isBlack(role)

    // UserDetails
    override fun isAccountNonLocked() = !isBlack(role)

    // UserDetails
    override fun isCredentialsNonExpired() = !isBlack(role)
    // UserDetails
    override fun isEnabled() = !isBlack(role)

}
