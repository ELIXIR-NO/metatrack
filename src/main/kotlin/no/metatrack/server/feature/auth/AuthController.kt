package no.metatrack.server.feature.auth

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class AuthController {
    @GetMapping("/me")
    fun getMyProfile(authentication: Authentication): Map<String, Any> {
        val jwt = authentication.principal as Jwt
        val userId = jwt.subject
        val username = jwt.getClaimAsString("preferred_username")
        val email = jwt.getClaimAsString("email")
        val givenName = jwt.getClaimAsString("given_name")
        val familyName = jwt.getClaimAsString("family_name")

        return mapOf(
            "userId" to userId,
            "username" to username,
            "email" to email,
            "firstName" to givenName,
            "lastName" to familyName,
            "isAuthenticated" to authentication.isAuthenticated,
        )
    }
}
