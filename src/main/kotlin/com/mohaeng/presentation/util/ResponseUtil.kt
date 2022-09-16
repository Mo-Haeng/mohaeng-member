package com.mohaeng.presentation.util

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.nio.charset.StandardCharsets.UTF_8
import javax.servlet.http.HttpServletResponse

/**
 * Created by ShinD on 2022/09/04.
 */
object ResponseUtil {

    fun sendError(json: String, response: HttpServletResponse, httpStatus: HttpStatus) {

        response.status = httpStatus.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = UTF_8.name()
        response.writer.println(json)
        response.writer.flush()
    }
}