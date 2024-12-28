package kr.co.vacgom.api.invitation.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.vacgom.api.global.common.dto.BaseResponse
import kr.co.vacgom.api.global.exception.error.ErrorResponse
import kr.co.vacgom.api.invitation.presentation.dto.InvitationDto

@Tag(name = "초대 코드 API(내부 로직 수정 예정 사용 X)")
interface InvitationApi {
    @Operation(
        summary = "초대 코드 생성 API",
        operationId = "createInvitationCode",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "", content = [Content(schema = Schema(implementation = InvitationDto.Response.Create::class))]),
            ApiResponse(responseCode = "400", description = "Bad Request", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ]
    )
    fun createInvitationCode(request: InvitationDto.Request.Create): BaseResponse<InvitationDto.Response.Create>

    @Operation(
        summary = "초대 코드 등록 API",
        operationId = "registerInvitationCode",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = ""),
            ApiResponse(responseCode = "400", description = "Bad Request", content = [Content(schema = Schema(implementation = ErrorResponse::class))]),
        ]
    )
    fun registerInvitationCode(request: InvitationDto.Request.Register)


    companion object {
        const val INVITATION = "/invitation"
    }
}