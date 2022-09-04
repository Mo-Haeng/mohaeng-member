package com.mohang.infrastructure.authentication.oauth2.userservice.provider.converter

import com.mohang.domain.enums.OAuth2Type
import com.mohang.domain.member.Member

/**
 * Created by ShinD on 2022/09/04.
 */
class NaverToMemberConverter : OAuth2AttributesToMemberConverter {

    override fun support(oAuth2Type: OAuth2Type) =
        OAuth2Type.NAVER == oAuth2Type

    override fun convert(attributes: Map<String, Any>): Member {
        TODO("Not yet implemented")
    }

}