package com.mohang.presentation

import com.mohang.presentation.argumentresolver.auth.Auth
import com.mohang.presentation.model.AuthMember
import com.mohang.query.dao.MemberDataDao
import com.mohang.query.data.MemberData
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by ShinD on 2022/09/06.
 */
@RestController
class RetrieveMyInfoRestController(

    private val memberDataDao: MemberDataDao,
) {

    private val log = KotlinLogging.logger { }

    @GetMapping("/api/my")
    fun myInfo(@Auth authMember: AuthMember) : ResponseEntity<MemberData> {

        log.info { "내 정보 조회 요청" }

        return ResponseEntity.ok(memberDataDao.findById(authMember.id))
    }
}