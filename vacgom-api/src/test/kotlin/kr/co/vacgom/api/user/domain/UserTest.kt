package kr.co.vacgom.api.user.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kr.co.vacgom.api.auth.oauth.enums.SocialLoginProvider
import kr.co.vacgom.api.user.domain.enums.UserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserTest : FunSpec({
    test("User 객체 정상 생성 테스트") {
        val nickname = "가나ht!"
        val socialId = "socialId"
        val provider = SocialLoginProvider.KAKAO
        val roles = arrayListOf(SimpleGrantedAuthority(UserRole.ROLE_USER.name))

        val user = User.create(
            nickname = nickname,
            socialId = socialId,
            provider = provider,
            roles = roles,
        )

        nickname shouldBe user.nickname
        socialId shouldBe user.socialId
        provider shouldBe user.provider
        roles shouldBe user.roles
    }

    context("User nickname 예외 테스트") {
        context("nickname이 두 글자 미만인 경우") {
            test("IllegalArgumentException 예외가 발생한다.") {
                val result = shouldThrow<IllegalArgumentException> {
                    User.create(
                        nickname = "1",
                        socialId = "socialId",
                        provider = SocialLoginProvider.KAKAO,
                        roles = emptyList(),
                    )
                }

                //Todo: 예외 메시지 상수화 후 테스트 코드 추가하기
            }
        }

        context("nickname에 영어 대문자가 포함된 경우") {
            test("IllegalArgumentException 예외가 발생한다.") {
                val result = shouldThrow<IllegalArgumentException> {
                    User.create(
                        nickname = "AB2!",
                        socialId = "socialId",
                        provider = SocialLoginProvider.KAKAO,
                        roles = emptyList(),
                    )
                }

                //Todo: 예외 메시지 상수화 후 테스트 코드 추가하기
            }
        }
    }
})