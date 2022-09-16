package com.mohaeng.configuration.security.enums

import org.springframework.http.HttpMethod

/**
 * Created by ShinD on 2022/08/31.
 */
enum class PermitAllURI(val method: HttpMethod?, val uri: String) {

    // 로그인 URI
    LOGIN(HttpMethod.POST, "/login"),

    // 로그인 URI
    OAUTH2_LOGIN(HttpMethod.GET, "/login/oauth2/**"),

    // 회원가입 URI
    SIGN_UP(HttpMethod.POST, "/api/member"),

    // 정보 조회
    SEARCH(HttpMethod.GET, "/api/member/**"),

    // 헬스 체크 URI
    HEALTH_CHECK(HttpMethod.GET, "/health-check"),

    // H2 URI
    H2(null, "/h2-console/**"),

    // 예외 URI
    ERROR(null, "/error"),

    // 메인
    MAIN(null, "/"),

    // 메인
    FAVICON(HttpMethod.GET, "/favicon.ico"),

    ;

    companion object {
        fun permitAllMap(): Map<HttpMethod?, List<PermitAllURI>> {
            return values().groupBy { it.method }
        }
    }
}