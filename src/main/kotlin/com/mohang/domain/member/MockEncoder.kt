package com.mohang.domain.member

import org.springframework.stereotype.Service

/**
 * Created by ShinD on 2022/08/30.
 */
@Service
class MockEncoder : MemberPasswordEncoder {

    override fun encode(rawPassword: String): String {
        return "!!"
    }

    override fun matches(rawPassword: String, encodedPassword: String): Boolean {
        TODO("Not yet implemented")
    }
}