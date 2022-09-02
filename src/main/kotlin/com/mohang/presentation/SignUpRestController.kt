package com.mohang.presentation

import com.mohang.application.member.usecase.SignUpUseCase
import com.mohang.presentation.model.SignUpRequest
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder.fromPath
import javax.validation.Valid

/**
 * Created by ShinD on 2022/08/30.
 */
@RestController
class SignUpRestController(

    var signUpUseCase: SignUpUseCase,

) {

    private val log = KotlinLogging.logger { }

    @PostMapping("/api/member")
    fun signUp(
        @Valid @RequestBody signUpRequest: SignUpRequest,
    ): ResponseEntity<Unit> {

        log.debug { "SignUpRestController.signUp()" }

        // 회원가입 진행
        val memberId = signUpUseCase.command(signUpRequest.toServiceDto())

        // 생성된 회원에 대한 url 생성
        val uri = fromPath("/api/member/{memberId}") // api/member/{memberId}
            .buildAndExpand(memberId)      // api/member/10
            .toUri()

        return ResponseEntity.created(uri).build()
    }
}