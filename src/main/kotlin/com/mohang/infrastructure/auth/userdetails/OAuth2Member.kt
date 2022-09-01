package com.mohang.infrastructure.auth.userdetails

import com.mohang.domain.enums.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.DefaultOAuth2User


/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2Member(

    private val id: Long,

    private val role: Role,

    attributes: Map<String, Any>,

    nameAttributeKey: String,

) : DefaultOAuth2User(
    setOf(SimpleGrantedAuthority(role.authority)),
    attributes,
    nameAttributeKey
)