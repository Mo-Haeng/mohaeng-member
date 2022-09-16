package com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter

import com.mohaeng.domain.enums.OAuth2Type
import com.mohaeng.domain.member.Member

/**
 * Created by ShinD on 2022/09/04.
 */
interface OAuth2AttributesToMemberConverter {

    fun support(oAuth2Type: OAuth2Type): Boolean

    fun convert(attributes: Map<String, Any>): Member
}