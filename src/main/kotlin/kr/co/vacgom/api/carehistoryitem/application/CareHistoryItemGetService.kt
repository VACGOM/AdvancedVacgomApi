package kr.co.vacgom.api.carehistoryitem.application

import kr.co.vacgom.api.baby.application.BabyQueryService
import kr.co.vacgom.api.carehistoryitem.domain.enums.CareHistoryItemType
import kr.co.vacgom.api.carehistoryitem.presentation.dto.CareHistoryDto
import kr.co.vacgom.api.carehistoryitem.repository.CareHistoryItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.*

@Service
@Transactional(readOnly = true)
class CareHistoryItemGetService(
    private val careHistoryItemRepository: CareHistoryItemRepository,
    private val babyQueryService: BabyQueryService
) {
    fun getCareHistoryByExecutionDate(babyId: UUID, executionDate: LocalDate): CareHistoryDto.Response {
        val findBaby = babyQueryService.getBabyById(babyId)
        val careHistory = careHistoryItemRepository.findByBabyAndExecutionDate(findBaby, executionDate)
        
        return CareHistoryDto.Response.DailyStat.of(careHistory)
    }

    fun getCareHistoryItemsByItemTypeAndExecutionDate(
        babyId: UUID,
        itemType: CareHistoryItemType,
        executionDate: LocalDate
    ): CareHistoryDto.Response {
        val findBaby = babyQueryService.getBabyById(babyId)
        val careHistoryItems = careHistoryItemRepository.findByBabyAndExecutionDateAndItemType(findBaby, executionDate, itemType)

        return CareHistoryDto.Response.DailyDetail.of(
            babyId = babyId,
            executionDate = executionDate,
            itemType = itemType,
            items = careHistoryItems
        )
    }
}
