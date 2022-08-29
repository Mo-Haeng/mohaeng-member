package com.mohang

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MohangMemberAuthApplication

fun main(args: Array<String>) {
    runApplication<MohangMemberAuthApplication>(*args)
}
