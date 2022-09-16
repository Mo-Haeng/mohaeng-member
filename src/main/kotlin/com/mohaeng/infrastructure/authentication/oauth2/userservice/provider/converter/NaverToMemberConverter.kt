package com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter

import com.mohaeng.domain.enums.OAuth2Type
import com.mohaeng.domain.member.Member

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