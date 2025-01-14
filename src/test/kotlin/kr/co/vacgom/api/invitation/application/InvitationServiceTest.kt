package kr.co.vacgom.api.invitation.application

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.types.shouldBeTypeOf
import io.mockk.every
import io.mockk.mockk
import kr.co.vacgom.api.auth.oauth.enums.SocialLoginProvider
import kr.co.vacgom.api.baby.application.BabyQueryService
import kr.co.vacgom.api.baby.domain.Baby
import kr.co.vacgom.api.baby.domain.enums.Gender
import kr.co.vacgom.api.babymanager.application.BabyManagerService
import kr.co.vacgom.api.babymanager.domain.BabyManager
import kr.co.vacgom.api.babymanager.exception.BabyManagerError
import kr.co.vacgom.api.global.exception.error.BusinessException
import kr.co.vacgom.api.global.util.UuidCreator
import kr.co.vacgom.api.invitation.domain.InvitationCode
import kr.co.vacgom.api.invitation.exception.InvitationError
import kr.co.vacgom.api.invitation.repository.InvitationRepository
import kr.co.vacgom.api.user.domain.User
import kr.co.vacgom.api.user.domain.enums.UserRole
import org.slf4j.Logger
import java.time.LocalDate
import java.util.*

class InvitationServiceTest : DescribeSpec({
    val invitationRepositoryMock: InvitationRepository = mockk(relaxed = true)
    val babyManagerServiceMock: BabyManagerService = mockk(relaxed = true)
    val babyQueryServiceMock: BabyQueryService = mockk(relaxed = true)
    val loggerMock: Logger = mockk(relaxed = true)

    val sut = InvitationService(
        invitationRepositoryMock,
        babyManagerServiceMock,
        babyQueryServiceMock,
        logger = loggerMock
    )

    describe("초대 코드 생성 테스트") {
        val adminUser = User(
            nickname = "nickname",
            socialId = "socialId",
            provider = SocialLoginProvider.KAKAO,
            role = UserRole.ROLE_USER,
        )

        val babies = enumValues<Gender>().map {
            Baby(
                name = "babyName",
                profileImg = "profileImg",
                gender = it,
                birthday = LocalDate.now(),
            )
        }

        val babyManagers = babies.map {
            BabyManager(
                user = adminUser,
                baby = it,
                isAdmin = true
            )
        }

        context("유저가 대표 돌보미로 존재하는 모든 아기에 대한 초대 코드를 생성한다면") {
            it("초대 코드가 생성된다.") {
                every { babyManagerServiceMock.getBabiesByUserIsAdmin(adminUser.id) } returns babies

                val result = sut.createInvitationCodeByUserIsAdmin(adminUser.id)

                result.invitationCode.shouldNotBeEmpty()
                result.invitationCode.shouldBeTypeOf<String>()
                UUID.fromString(result.invitationCode).shouldBeTypeOf<UUID>()
            }
        }

        context("유저가 대표 돌보미로 존재하는 한 명의 아기에 대한 초대 코드를 생성한다면") {
            it("초대 코드가 생성된다.") {
                every { babyManagerServiceMock.getBabyByIdAndUserIsAdmin(adminUser.id, babies[0].id) } returns babies[0]

                val result = sut.createInvitationCodeByBabyId(adminUser.id, babies[0].id)

                result.invitationCode.shouldNotBeEmpty()
                result.invitationCode.shouldBeTypeOf<String>()
                UUID.fromString(result.invitationCode).shouldBeTypeOf<UUID>()
            }
        }

        context("유저가 초대 코드를 생성하고자 하는 아기의 대표 돌보미가 아닌 경우") {
            it("${BabyManagerError.NOT_ADMIN_BABY_MANAGER} 예외가 발생한다.") {
                every {
                    babyManagerServiceMock.getBabyByIdAndUserIsAdmin(adminUser.id, babies[0].id)
                } throws BusinessException(BabyManagerError.NOT_ADMIN_BABY_MANAGER)

                val result = shouldThrow<BusinessException> { sut.createInvitationCodeByBabyId(adminUser.id, babies[0].id) }

                result.errorCode shouldBe BabyManagerError.NOT_ADMIN_BABY_MANAGER
            }
        }
    }

    describe("초대 코드 조회 테스트") {
        val adminUser = User(
            nickname = "nickname",
            socialId = "socialId",
            provider = SocialLoginProvider.KAKAO,
            role = UserRole.ROLE_USER,
        )

        val babies = enumValues<Gender>().map {
            Baby(
                name = "babyName",
                profileImg = "profileImg",
                gender = it,
                birthday = LocalDate.now(),
            )
        }

        val invitationCode = InvitationCode(
            key = UuidCreator.create().toString(),
            userId = adminUser.id,
            babyIds = babies.map { it.id },
        )

        context("초대 코드를 정상적으로 조회한다면") {
            it("해당하는 아기 상세 정보를 반환한다.") {
                every { invitationRepositoryMock.getInvitationCodeAndUpdateExpired(invitationCode.key) } returns invitationCode
                every { babyQueryServiceMock.getBabiesById(babies.map { it.id }) } returns babies

                val result = sut.getBabiesByInvitationCode(invitationCode.key)

                result.forEachIndexed { index, detail ->
                    detail.id shouldBe babies[index].id
                    detail.name shouldBe babies[index].name
                    detail.profileImg shouldBe babies[index].profileImg
                    detail.gender shouldBe babies[index].gender
                    detail.birthday shouldBe babies[index].birthday
                }
            }
        }

        context("초대 코드가 존재하지 않는다면") {
            it("${InvitationError.INVITATION_CODE_NOT_FOUND} 예외가 발생한다.") {
                every {
                    invitationRepositoryMock.getInvitationCodeAndUpdateExpired(invitationCode.key)
                } throws BusinessException(InvitationError.INVITATION_CODE_NOT_FOUND)

                val result = shouldThrow<BusinessException> { sut.getBabiesByInvitationCode(invitationCode.key) }

                result.errorCode shouldBe InvitationError.INVITATION_CODE_NOT_FOUND
            }
        }
    }
})
