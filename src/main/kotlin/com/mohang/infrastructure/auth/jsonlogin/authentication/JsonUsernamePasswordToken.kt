package com.mohang.infrastructure.auth.jsonlogin.authentication

import com.mohang.domain.enums.Role
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

/**
 * Created by ShinD on 2022/09/01.
 */
class JsonUsernamePasswordToken : AbstractAuthenticationToken {

    /**
     * 인증 시도 요청 시 : username
     * 인증 완료 시 : AuthMember
     */
    private var principal: Any

    private var credentials: Any? // password가 담김

    /**
     * 인증 시도 요청 시 생성자
     */
    constructor(principal: Any, credentials: Any?) : super(null) {
        this.principal = principal
        this.credentials = credentials
        isAuthenticated = false
    }

    /**
     * 인증 완료 시 생성자
     */
    constructor(principal: Any, credentials: Any?, role: Role) : super(setOf(SimpleGrantedAuthority(role.authority))) {
        this.principal = principal
        this.credentials = credentials
        isAuthenticated = true
    }

    override fun getPrincipal() = principal

    override fun getCredentials() = credentials

    override fun eraseCredentials() {
        super.eraseCredentials()
        this.credentials = null
    }
}