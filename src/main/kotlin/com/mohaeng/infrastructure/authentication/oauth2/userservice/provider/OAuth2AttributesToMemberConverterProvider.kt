package com.mohaeng.infrastructure.authentication.oauth2.userservice.provider

import com.mohaeng.domain.enums.OAuth2Type
import com.mohaeng.domain.member.Member
import com.mohaeng.infrastructure.authentication.oauth2.userservice.provider.converter.OAuth2AttributesToMemberConverter

/**
 * Created by ShinD on 2022/09/01.
 */
class OAuth2AttributesToMemberConverterProvider(

    private val converters: List<OAuth2AttributesToMemberConverter>,
) {

    fun convert(oAuth2Type: OAuth2Type, attributes: Map<String, Any>): Member  {

        // OAuth2 타입을 지원하는 컨버터를 찾아서 변환
        for (converter in converters) {
            if (converter.support(oAuth2Type) ) {
                return converter.convert(attributes)
            }
        }
        throw RuntimeException("등록되지 않은 OAuth2 서비스입니다.")
    }
}