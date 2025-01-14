package kr.co.vacgom.api.invitation.repository

import kr.co.vacgom.api.invitation.domain.InvitationCode

interface InvitationRepository {
    fun save(invitationCode: InvitationCode, ttl: Long)
    fun getInvitationCodeAndUpdateExpired(code: String): InvitationCode
    fun rollBackInvitationCodeExpired(code: String)
}
