package kr.co.vacgom.api.carehistory.domain

import kr.co.vacgom.api.carehistory.domain.enums.BreastDirection
import kr.co.vacgom.api.global.common.domain.BaseTimeEntity
import java.time.LocalDateTime

data class BreastFeeding (
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val duration: Long,
    val breastDirection: BreastDirection
): BaseTimeEntity() {
    companion object {
        fun create(
            startDate: LocalDateTime,
            endDate: LocalDateTime,
            duration: Long,
            breastDirection: BreastDirection
        ): BreastFeeding {
            return BreastFeeding(
                startDate = startDate,
                endDate = endDate,
                duration = duration,
                breastDirection = breastDirection
            )
        }
    }
}
