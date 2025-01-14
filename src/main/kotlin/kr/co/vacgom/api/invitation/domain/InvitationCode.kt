package kr.co.vacgom.api.invitation.domain

import java.time.LocalDateTime
import java.util.*

data class InvitationCode(
    val key: String,
    val userId: UUID,
    val babyIds: List<UUID>,
    var isExpired: Boolean = false,
    val timeToLive: Long = 86400L,
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
