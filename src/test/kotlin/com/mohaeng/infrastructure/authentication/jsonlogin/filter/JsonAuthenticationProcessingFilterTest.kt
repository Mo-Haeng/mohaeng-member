package com.mohaeng.infrastructure.authentication.jsonlogin.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.mohaeng.domain.enums.Role
import com.mohaeng.infrastructure.authentication.jsonlogin.authentication.JsonUsernamePasswordToken
import io.mockk.every
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import strikt.api.expectThrows
import strikt.assertions.isEqualTo
import strikt.assertions.message
import javax.security.auth.message.AuthException

/**
 * Created by ShinD on 2022/09/01.
 */
internal class JsonAuthenticationProcessingFilterTest {

    /**
     *
     * post가 아닌 경우 예외
     *
     * Json이 아닌 경우 예외
     *
     * body에 아무것도 쓰여있지 않다면 예외
     *
     * username이 공백이라면 예외
     * password가 공백이라면 예외
     *
     * 모두 정상인 경우 authenticationManager.authenticate(JsonUsernamePasswordToken) 호출
     */
    private val jsonAuthenticationProcessingFilter = JsonAuthenticationProcessingFilter("/login", ObjectMapper())


    @Test
    fun `post가 아닌 경우 예외`() {

        //given
        val req = MockHttpServletRequest()
        req.method = HttpMethod.GET.name
        val res = MockHttpServletResponse()

        //when
        expectThrows<AuthException> { jsonAuthenticationProcessingFilter.attemptAuthentication(req, res) }
            //then
            .message.isEqualTo("로그인은 POST 메서드로 진행해야 합니다.")
    }

    @Test
    fun `Json이 아닌 경우 예외`() {

        //given
        val req = MockHttpServletRequest()
        req.method = HttpMethod.POST.name
        req.contentType = MediaType.TEXT_PLAIN.type
        val res = MockHttpServletResponse()

        //when
        expectThrows<AuthException> { jsonAuthenticationProcessingFilter.attemptAuthentication(req, res) }
            //then
            .message.isEqualTo("로그인은 JSON으로 진행해야 합니다.")
    }

    @Test
    fun `body에 아무것도 쓰여있지 않다면 예외`() {

        //given
        val req = MockHttpServletRequest()
        req.method = HttpMethod.POST.name
        req.contentType = MediaType.APPLICATION_JSON_VALUE
        val res = MockHttpServletResponse()

        //when
        expectThrows<AuthException> { jsonAuthenticationProcessingFilter.attemptAuthentication(req, res) }
            //then
            .message.isEqualTo("아이디 혹은 비밀번호가 입력되지 않았습니다.")
    }

    @Test
    fun `username이 공백이라면 예외`() {

        //given
        val req = MockHttpServletRequest()
        req.method = HttpMethod.POST.name
        req.contentType = MediaType.APPLICATION_JSON_VALUE
        req.setContent("""{"username":"   ","password":"111"}""".toByteArray())
        val res = MockHttpServletResponse()

        //when
        expectThrows<AuthException> { jsonAuthenticationProcessingFilter.attemptAuthentication(req, res) }
            //then
            .message.isEqualTo("아이디 혹은 비밀번호가 입력되지 않았습니다.")
    }

    @Test
    fun `password가 공백이라면 예외`() {

        //given
        val req = MockHttpServletRequest()
        req.method = HttpMethod.POST.name
        req.contentType = MediaType.APPLICATION_JSON_VALUE
        req.setContent("""{"username":"sdwq","password":"   "}""".toByteArray())
        val res = MockHttpServletResponse()

        //when
        expectThrows<AuthException> { jsonAuthenticationProcessingFilter.attemptAuthentication(req, res) }
            //then
            .message.isEqualTo("아이디 혹은 비밀번호가 입력되지 않았습니다.")
    }

    @Test
    fun `모두 정상인 경우 인증 진행`() {

        //given
        val mockAuthManager = mockkClass(AuthenticationManager::class)
        jsonAuthenticationProcessingFilter.setAuthenticationManager(mockAuthManager)
        every { mockAuthManager.authenticate(any()) } returns JsonUsernamePasswordToken(
            principal = "principal",
            credentials = "credentials",
            role = Role.BASIC,
        )

        val req = MockHttpServletRequest()
        req.method = HttpMethod.POST.name
        req.contentType = MediaType.APPLICATION_JSON_VALUE
        req.setContent("""{"username":"sdwq","password":"dwdw"}""".toByteArray())
        val res = MockHttpServletResponse()

        //when
        jsonAuthenticationProcessingFilter.attemptAuthentication(req, res)

        //then
        verify (exactly = 1) { mockAuthManager.authenticate(any()) }
    }
}