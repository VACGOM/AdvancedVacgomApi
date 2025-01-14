package kr.co.vacgom.api.invitation.repository

import kr.co.vacgom.api.global.exception.error.BusinessException
import kr.co.vacgom.api.invitation.domain.InvitationCode
import kr.co.vacgom.api.invitation.exception.InvitationError
import org.springframework.stereotype.Repository

@Repository
class InvitationRepositoryAdapter(
    private val invitationRedisRepository: InvitationRedisRepository,
): InvitationRepository {
    override fun save(invitationCode: InvitationCode, ttl: Long) {
        invitationRedisRepository.save(invitationCode, ttl)
    }

    override fun getInvitationCodeAndUpdateExpired(code: String): InvitationCode {
        return invitationRedisRepository.getInvitationCodeAndUpdateExpired(code) ?:
            throw BusinessException(InvitationError.INVITATION_CODE_NOT_FOUND)
    }

    override fun rollBackInvitationCodeExpired(code: String) {
        invitationRedisRepository.rollBackExpiredInvitationCode(code)
    }
}
