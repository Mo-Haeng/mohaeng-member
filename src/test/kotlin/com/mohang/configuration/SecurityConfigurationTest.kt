package com.mohang.configuration

import com.mohang.application.usecase.SignUpUseCase
import com.mohang.configuration.security.SecurityConfiguration
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import strikt.api.expectThat
import strikt.assertions.isNotEqualTo

/**
 * Created by ShinD on 2022/09/01.
 */
@WebMvcTest
@Import(SecurityConfiguration::class)
@MockkBean(classes = [SignUpUseCase::class])
class SecurityConfigurationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc


    @Test
    fun `login POST permit all`() {

        mockMvc.post("/login")
            //then
            .andExpect {
                status { isNotFound() }
            }
    }

    @Test
    fun `signUp POST permit all`() {

        val result = mockMvc.post("/api/member")
            .andReturn()

        // 401(UNAUTHORIZED)이 아니어야 함
        expectThat(result.response.status).isNotEqualTo(UNAUTHORIZED.value())
    }

    @Test
    fun `healthCheck GET permit all`() {

        val result = mockMvc.get("/health-check")
            .andReturn()

        // 401(UNAUTHORIZED)이 아니어야 함
        expectThat(result.response.status).isNotEqualTo(UNAUTHORIZED.value())
    }

    @Test
    fun `h2 permit all`() {

        val getResult = mockMvc.get("/h2-console")
            .andReturn()

        // 401(UNAUTHORIZED)이 아니어야 함
        expectThat(getResult.response.status).isNotEqualTo(UNAUTHORIZED.value())

        val postResult = mockMvc.post("/h2-console")
            .andReturn()

        // 401(UNAUTHORIZED)이 아니어야 함
        expectThat(postResult.response.status).isNotEqualTo(UNAUTHORIZED.value())
    }

    @Test
    fun `error permit all`() {

        val getResult = mockMvc.get("/error")
            .andReturn()

        // 401(UNAUTHORIZED)이 아니어야 함
        expectThat(getResult.response.status).isNotEqualTo(UNAUTHORIZED.value())

        val postResult = mockMvc.post("/error")
            .andReturn()

        // 401(UNAUTHORIZED)이 아니어야 함
        expectThat(postResult.response.status).isNotEqualTo(UNAUTHORIZED.value())
    }
}