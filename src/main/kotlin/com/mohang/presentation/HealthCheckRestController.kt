package com.mohang.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by ShinD on 2022/09/04.
 */
@RestController
class HealthCheckRestController {

    @GetMapping("/health-check")
    fun health() =
        ResponseEntity.ok("Server is Running")
}