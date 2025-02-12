package kr.co.vacgom.api.auth.oauth.kakao.config

import kr.co.vacgom.api.auth.oauth.kakao.KakaoProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KakaoProperties::class)
class KakaoPropertiesConfig {

}
