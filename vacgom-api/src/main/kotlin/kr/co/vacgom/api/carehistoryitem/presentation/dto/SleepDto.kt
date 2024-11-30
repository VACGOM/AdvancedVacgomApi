package kr.co.vacgom.api.carehistoryitem.presentation.dto

import kr.co.vacgom.api.carehistoryitem.domain.Sleep
import kr.co.vacgom.api.carehistoryitem.domain.enums.CareHistoryItemType
import java.time.LocalDateTime
import java.util.*

class SleepDto {
    data class Request(
        val babyId: UUID,
        val startDate: LocalDateTime,
        val endDate: LocalDateTime,
        val executionDate: LocalDateTime,
    )

    class Response {
        class DailyStat(
            careName: String,
            val hours: Int,
            val minutes: Int,
        ): AbstractDailyStatDto(careName) {
            companion object {
                fun of(type: CareHistoryItemType, items: List<Sleep>): DailyStat {
                    val totalMinutes = items.sumOf { it.minutes }

                    return DailyStat(
                        careName = type.typeName,
                        hours = totalMinutes / 60,
                        minutes = totalMinutes % 60
                    )
                }
            }
        }
    }
}