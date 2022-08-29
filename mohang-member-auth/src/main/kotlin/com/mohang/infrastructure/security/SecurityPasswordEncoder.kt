package com.mohang.infrastructure.security

import com.mohang.domain.MemberPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

/**
 * Created by ShinD on 2022/08/30.
 */
class SecurityPasswordEncoder (

    private val passwordEncoder: PasswordEncoder

) : MemberPasswordEncoder {

    override fun encode(rawPassword: String): String =
        passwordEncoder.encode(rawPassword)

    override fun matches(rawPassword: String, encodedPassword: String): Boolean =
        passwordEncoder.matches(rawPassword, encodedPassword)
}