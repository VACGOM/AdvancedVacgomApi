package kr.co.vacgom.api.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.co.vacgom.api.global.exception.error.BusinessException
import kr.co.vacgom.api.global.exception.error.GlobalError
import kr.co.vacgom.api.global.presentation.GlobalPath
import kr.co.vacgom.api.global.security.SecurityContext
import kr.co.vacgom.api.global.security.SecurityContextHolder
import kr.co.vacgom.api.global.security.UserAuthentication
import kr.co.vacgom.api.member.application.MemberTokenService
import kr.co.vacgom.api.member.presentation.AuthPath
import kr.co.vacgom.api.member.presentation.MemberPath
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val memberTokenService: MemberTokenService,
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        memberTokenService.extractToken(request)?.run {
            val accessUserId = memberTokenService.resolveToken(this)
            val userAuthentication = UserAuthentication(accessUserId)
            SecurityContextHolder.setContext(SecurityContext(userAuthentication))
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
            GlobalPath.BASE_V3 + MemberPath.MEMBER to HttpMethod.POST,
        )
    }
}
