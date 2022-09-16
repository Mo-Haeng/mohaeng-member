package com.mohaeng.infrastructure.jwt.usecase.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * Created by ShinD on 2022/09/02.
 */
@ConstructorBinding
@ConfigurationProperties("jwt")
data class JwtProperties (

    val secretKey: String,

    val authTokenExpirationPeriodDay: Long,

    val refreshTokenExpirationPeriodDay: Long,

)