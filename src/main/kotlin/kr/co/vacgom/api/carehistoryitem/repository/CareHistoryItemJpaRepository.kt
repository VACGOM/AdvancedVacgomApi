package kr.co.vacgom.api.carehistoryitem.repository

import kr.co.vacgom.api.baby.domain.Baby
import kr.co.vacgom.api.carehistoryitem.domain.CareHistoryItem
import kr.co.vacgom.api.carehistoryitem.domain.enums.CareHistoryItemType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface CareHistoryItemJpaRepository : JpaRepository<CareHistoryItem, Long> {
    fun findByBabyAndExecutionTimeBetween(baby: Baby, startExecutionDate: LocalDateTime, endExecutionDate: LocalDateTime): List<CareHistoryItem>
    fun findByBabyAndItemTypeAndExecutionTimeBetween(
        baby: Baby,
        itemType: CareHistoryItemType,
        startExecutionDate: LocalDateTime,
        endExecutionDate: LocalDateTime
    ) : List<CareHistoryItem>
}
