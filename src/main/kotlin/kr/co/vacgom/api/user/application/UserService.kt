package kr.co.vacgom.api.user.application

import kr.co.vacgom.api.baby.Baby
import kr.co.vacgom.api.baby.application.BabyService
import kr.co.vacgom.api.babymanager.BabyManager
import kr.co.vacgom.api.babymanager.application.BabyManagerService
import kr.co.vacgom.api.global.exception.error.BusinessException
import kr.co.vacgom.api.user.User
import kr.co.vacgom.api.user.domain.enums.UserRole
import kr.co.vacgom.api.user.exception.UserError
import kr.co.vacgom.api.user.presentation.dto.Signup
import kr.co.vacgom.api.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userTokenService: UserTokenService,
    private val userRepository: UserRepository,
    private val authService: AuthService,
    private val babyService: BabyService,
    private val babyManagerService: BabyManagerService,
) {
    fun signup(request: Signup.Request): Signup.Response {
        val registerToken = userTokenService.resolveRegisterToken(request.registerToken)

        val newUser = User(
            request.nickname,
            registerToken.socialId,
            registerToken.provider,
            UserRole.ROLE_USER,
        )

        val newBabies = request.babies.map {
            Baby(
                it.name,
                it.profileImgUrl,
                it.gender,
                it.birthday,
            )
        }

        val savedUser = userRepository.save(newUser)
        val savedBabies = babyService.saveAll(newBabies)

        val managers = savedBabies.map { baby ->
            BabyManager(
                savedUser,
                baby,
                true
            )
        }

        babyManagerService.saveAll(managers)

        val refreshToken = userTokenService.createRefreshToken(savedUser.id)
        userTokenService.saveRefreshToken(refreshToken, savedUser.id)

        return Signup.Response(
            accessToken = userTokenService.createAccessToken(savedUser.id, savedUser.role),
            refreshToken = refreshToken,
        )
    }

    fun revoke(userId: Long) {
        val findUser = userRepository.findById(userId)
            ?: throw BusinessException(UserError.USER_NOT_FOUND)

        authService.unlinkUser(findUser)
        userRepository.deleteById(userId)
    }
}
