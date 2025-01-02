package kr.co.vacgom.api.baby.presentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.co.vacgom.api.baby.presentation.dto.BabyDto
import kr.co.vacgom.api.global.common.dto.BaseResponse
import kr.co.vacgom.api.global.exception.error.ErrorResponse
import java.util.*

@Tag(name = "아기 API")
interface BabyApi {
    @Operation(
        summary = "아기 프로필 이미지 업로드 API",
        operationId = "uploadBabyImage",
        description = """""",
        responses = [
            ApiResponse(responseCode = "200", description = "OK"),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
        ]
    )
    fun uploadBabyImage(
        @Parameter(description = "이미지 바이너리 데이터")
        request: BabyDto.Request.UploadImage
    ): BaseResponse<List<BabyDto.Response.UploadedImage>>

    @Operation(
        summary = "아기 상세정보(나이 포함 가능) 조회 API",
        operationId = "getBabyDetail",
        description = """""",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [Content(schema = Schema(implementation = BabyDto.Response.Detail::class))]
            ),
            ApiResponse(
                responseCode = "200",
                description = "OK",
                content = [Content(schema = Schema(implementation = BabyDto.Response.DetailWithAge::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad Request",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            ),
        ]
    )
    fun getBabyDetail(babyId: UUID, withAge: Boolean?): BaseResponse<BabyDto.Response>

    companion object {
        const val BABY = "/babies"
    }
}
