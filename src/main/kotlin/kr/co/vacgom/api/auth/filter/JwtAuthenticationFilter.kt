package kr.co.vacgom.api.auth.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.vacgom.api.global.exception.error.BusinessException
import kr.co.vacgom.api.global.exception.error.GlobalError
import kr.co.vacgom.api.global.presentation.GlobalPath
import kr.co.vacgom.api.auth.security.UserAuthentication
import kr.co.vacgom.api.user.application.UserTokenService
import kr.co.vacgom.api.user.presentation.AuthPath
import kr.co.vacgom.api.user.presentation.UserPath
import org.springframework.http.HttpMethod
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val userTokenService: UserTokenService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        userTokenService.extractToken(request)?.run {
            val accessToken = userTokenService.resolveAccessToken(this)
            val userAuthentication = UserAuthentication(accessToken.userId, accessToken.authorities)

            SecurityContextHolder.getContext().authentication = userAuthentication
        } ?: throw BusinessException(GlobalError.UNAUTHORIZED)

        filterChain.doFilter(request, response)
        SecurityContextHolder.clearContext()
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return ignoredPath.any { (ignoredPathURI, method) ->
            val matchedPath = AntPathMatcher().matchStart(ignoredPathURI, request.requestURI)
            val matchedMethod = method.matches(request.method)
            matchedPath && matchedMethod
        }
    }

    companion object {
        private val ignoredPath: Map<String, HttpMethod> = mapOf(
            GlobalPath.BASE_V3 + AuthPath.AUTH.plus("/login/**") to HttpMethod.POST,
            "/actuator/health" to HttpMethod.GET,
            GlobalPath.BASE_V3 + UserPath.USER to HttpMethod.POST,
            GlobalPath.BASE_V3.plus("/TEST/**") to HttpMethod.POST
        )
    }
}