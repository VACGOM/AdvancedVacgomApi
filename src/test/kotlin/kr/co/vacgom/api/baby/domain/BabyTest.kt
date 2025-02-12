package kr.co.vacgom.api.baby.domain

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kr.co.vacgom.api.baby.domain.enums.Gender
import java.time.LocalDate

class BabyTest : FunSpec({
    test("Baby 객체 정상 생성 테스트") {
        val name = "name"
        val profileImg = "profileImgUrl"
        val gender = Gender.MALE
        val birthday = LocalDate.now()

        val baby = Baby(
            name = name,
            profileImg = profileImg,
            gender = gender,
            birthday = birthday,
        )

        baby.name shouldBe name
        baby.profileImg shouldBe profileImg
        baby.gender shouldBe gender
        baby.birthday shouldBe birthday

        //Todo: 추후 Baby 도메인 제약 사항 추가 시 검증 필요
    }

    test("Baby 객체 update 테스트") {
        val updateName = "name"
        val updateProfileImg = "profileImgUrl"
        val updateGender = Gender.MALE
        val updateBirthday = LocalDate.of(2022, 1, 1)

        val baby = Baby(
            name = "name",
            profileImg = "profileImg",
            gender = Gender.MALE,
            birthday = LocalDate.now(),
        )

        baby.update(
            name = updateName,
            profileImg = updateProfileImg,
            gender = updateGender,
            birthday = updateBirthday,
        )

        baby.name shouldBe updateName
        baby.profileImg shouldBe updateProfileImg
        baby.gender shouldBe updateGender
        baby.birthday shouldBe updateBirthday
    }
})
