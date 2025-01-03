package kr.co.vacgom.api.user.presentation.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.co.vacgom.api.baby.domain.enums.Gender
import java.time.LocalDate

class SignupDto {
    @Schema(name = "SignupDto.Request")
    data class Request(
        val registerToken: String,
        val nickname: String,
        val babies: List<Baby>,
    ) {
        data class Baby(
            val name: String,
            val gender: Gender,
            val profileImg: String?,
            val birthday: LocalDate,
            val isAdmin: Boolean,
        )
    }

    @Schema(name = "SignupDto.Response")
    data class Response(
        val accessToken: String,
        val refreshToken: String,
    )
}
