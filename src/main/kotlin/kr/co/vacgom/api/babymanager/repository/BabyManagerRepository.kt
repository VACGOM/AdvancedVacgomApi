package kr.co.vacgom.api.babymanager.repository

import kr.co.vacgom.api.babymanager.domain.BabyManager
import java.util.*


interface BabyManagerRepository {
    fun save(babyManager: BabyManager): BabyManager
    fun saveAll(managers: Collection<BabyManager>): List<BabyManager>
    fun findByUserIdAndIsAdmin(userId: UUID): Set<BabyManager>
}
