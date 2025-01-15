package kr.co.vacgom.api.carehistoryitem.repository

import kr.co.vacgom.api.baby.domain.Baby
import kr.co.vacgom.api.carehistoryitem.domain.CareHistory
import kr.co.vacgom.api.carehistoryitem.domain.CareHistoryItem
import kr.co.vacgom.api.carehistoryitem.domain.enums.CareHistoryItemType
import java.time.LocalDate

interface CareHistoryItemRepository {
    fun saveHistoryItem(item: CareHistoryItem)
    fun findByBabyAndExecutionDate(baby: Baby, executionDate: LocalDate): CareHistory
    fun findByBabyAndExecutionDateAndItemType(baby: Baby, executionDate: LocalDate, itemType: CareHistoryItemType): List<CareHistoryItem>
}
