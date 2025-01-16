package kr.co.vacgom.api.user.exception

import kr.co.vacgom.api.global.exception.error.ErrorCode
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND

enum class AuthError(
    override val message: String,
    override val status: HttpStatus,
    override val code: String,
) : ErrorCode {
    FEIGN_UNAUTHORIZED("유효하지 않은 엑세스 토큰 입니다.", NOT_FOUND, "A_001"),
}
