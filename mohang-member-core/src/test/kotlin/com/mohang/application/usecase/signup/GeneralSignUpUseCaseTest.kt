package com.mohang.application.usecase.signup

import com.mohang.application.exception.DuplicateUsernameException
import com.mohang.domain.MemberPasswordEncoder
import com.mohang.domain.RegistrationId
import com.mohang.domain.enums.RegistrationType.GENERAL
import com.mohang.fixture.MemberFixture.generalSignUpDto
import com.mohang.fixture.MemberFixture.savedMember
import com.mohang.infrastructure.persistence.MemberRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkClass
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.support.TransactionTemplate
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

/**
 * Created by ShinD on 2022/08/30.
 */


//@JdbcTest
@ExtendWith(SpringExtension::class)
@AutoConfigureJdbc
internal class GeneralSignUpUseCaseTest {

    private var memberRepository: MemberRepository = mockkClass(MemberRepository::class)

    @Autowired
    private lateinit var transaction: TransactionTemplate

    private lateinit var signUpUseCase: GeneralSignUpUseCase

    @MockK
    private var passwordEncoder: MemberPasswordEncoder = mockkClass(MemberPasswordEncoder::class)

    @BeforeEach
    fun setUp(){
        signUpUseCase = GeneralSignUpUseCase(memberRepository, transaction, passwordEncoder)
    }

    @Test
    fun `일반 회원가입`() {

        //given
        val username = "sample username"
        val encodedPw = "{encoded} pw"
        val general = RegistrationId(registrationType = GENERAL, registrationIdValue = username)
        val savedMember = savedMember(registrationId = general, password = encodedPw)

        val generalSignUpDto = generalSignUpDto(username = username)
        every { memberRepository.findByRegistrationId(general) } returns null
        every { passwordEncoder.encode(generalSignUpDto.password) } returns encodedPw
        every { memberRepository.save(any()) } returns savedMember


        //when
        val savedId = signUpUseCase.command(generalSignUpDto)

        //then
        expectThat(savedId).isEqualTo(savedMember.id)

        verify (exactly = 1){ memberRepository.findByRegistrationId(general) }
        verify (exactly = 1){ passwordEncoder.encode(generalSignUpDto.password) }
        verify (exactly = 1){ memberRepository.save(any()) }
    }




    @Test
    fun `일반 회원가입 실패 - 아이디 중복`() {

        //given
        val username = "sample username"
        val general = RegistrationId(registrationType = GENERAL, registrationIdValue = username)

        val generalSignUpDto = generalSignUpDto(username = username)
        every { memberRepository.findByRegistrationId(general) } throws DuplicateUsernameException()

        //when
        expectThrows<DuplicateUsernameException> { signUpUseCase.command(generalSignUpDto) }

        //then
        verify (exactly = 0){ memberRepository.save(any()) }
    }
}