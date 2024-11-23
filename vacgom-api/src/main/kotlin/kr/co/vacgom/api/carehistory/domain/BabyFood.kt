package kr.co.vacgom.api.carehistory.domain

import kr.co.vacgom.api.global.common.domain.BaseTimeEntity
import java.time.LocalDateTime

data class BabyFood (
    val amount: Long,
    val executionDate: LocalDateTime,
): BaseTimeEntity()
