package com.mohang.application.usecase

import com.mohang.application.exception.DuplicateUsernameException
import com.mohang.application.usecase.dto.SignUpDto
import com.mohang.domain.enums.SocialLoginType.NONE
import com.mohang.domain.member.Member
import com.mohang.domain.member.MemberPasswordEncoder
import com.mohang.domain.member.SocialLoginId
import com.mohang.fixture.MemberFixture.basicSignUpDto
import com.mohang.fixture.MemberFixture.notSavedMember
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
class SignUpUseCaseTest {

    private var memberRepository: MemberRepository = mockkClass(MemberRepository::class)

    @Autowired
    private lateinit var transaction: TransactionTemplate

    private lateinit var signUpUseCase: SignUpUseCase<SignUpDto>

    @MockK
    private var passwordEncoder: MemberPasswordEncoder = mockkClass(MemberPasswordEncoder::class)

    @BeforeEach
    fun setUp(){
        signUpUseCase = SignUpUseCase(memberRepository, transaction, passwordEncoder)
    }

    @Test
    fun `일반 회원가입`() {

        //given
        val username = "sample username"
        val encodedPw = "{encoded} pw"
        val general = SocialLoginId(socialLoginType = NONE, value = username)
        val savedMember = savedMember(socialLoginId = general, password = encodedPw)

        val basicSignUpDto = basicSignUpDto(username = username)
        every { memberRepository.findBySocialLoginId(general) } returns null
        every { passwordEncoder.encode(basicSignUpDto.password) } returns encodedPw
        every { memberRepository.save(any()) } returns savedMember


        //when
        expectThat(signUpUseCase.command(basicSignUpDto))
            .isEqualTo(savedMember.id)

        //then
        verify (exactly = 1){ memberRepository.findBySocialLoginId(general) }
        verify (exactly = 1){ passwordEncoder.encode(basicSignUpDto.password) }
        verify (exactly = 1){ memberRepository.save(any()) }
    }


    @Test
    fun `일반 회원가입 실패 - 아이디 중복`() {

        //given
        val username = "sample username"
        val general = SocialLoginId(socialLoginType = NONE, value = username)

        val basicSignUpDto = basicSignUpDto(username = username)
        every { memberRepository.findBySocialLoginId(general) } returns notSavedMember()

        //when
        expectThrows<DuplicateUsernameException> { signUpUseCase.command(basicSignUpDto) }

        //then
        verify (exactly = 0){ memberRepository.save(any()) }
    }
}