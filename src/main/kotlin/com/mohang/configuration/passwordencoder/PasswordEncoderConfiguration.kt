package com.mohang.configuration.passwordencoder

import com.mohang.domain.member.MemberPasswordEncoder
import com.mohang.infrastructure.passwordencoder.SecurityMemberPasswordEncoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by ShinD on 2022/09/01.
 */
@Configuration
class PasswordEncoderConfiguration {

    /**
     * MemberPasswordEncoder로는 SecurityMemberPasswordEncoder 사용
     */
    @Bean
    fun memberPasswordEncoder(): MemberPasswordEncoder =
        SecurityMemberPasswordEncoder()

}