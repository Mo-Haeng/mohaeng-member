package com.mohang

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MohangMemberJwtApplication

fun main(args: Array<String>) {
    runApplication<MohangMemberJwtApplication>(*args)
}
