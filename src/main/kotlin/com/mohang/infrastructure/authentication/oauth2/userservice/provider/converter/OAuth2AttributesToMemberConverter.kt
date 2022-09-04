package com.mohang.infrastructure.authentication.oauth2.userservice.provider.converter

import com.mohang.domain.enums.OAuth2Type
import com.mohang.domain.member.Member

/**
 * Created by ShinD on 2022/09/04.
 */
interface OAuth2AttributesToMemberConverter {

    fun support(oAuth2Type: OAuth2Type): Boolean

    fun convert(attributes: Map<String, Any>): Member
}