package no.metatrack.server.feature.auth

import no.metatrack.server.feature.project.ProjectAuthorizationService
import no.metatrack.server.feature.project.ProjectRole
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.UUID

@Component
class CustomProjectPermissionEvaluator(
    private val projectAuthService: ProjectAuthorizationService,
) : PermissionEvaluator {
    override fun hasPermission(
        authentication: Authentication,
        targetDomainObject: Any,
        permission: Any,
    ): Boolean {
        if (targetDomainObject is UUID && permission is ProjectRole) {
            val jwt =
                authentication.principal as? org.springframework.security.oauth2.jwt.Jwt
                    ?: return false
            return projectAuthService.hasPermission(targetDomainObject, jwt.subject, permission)
        }

        return true
    }

    override fun hasPermission(
        authentication: Authentication,
        targetId: Serializable,
        targetType: String,
        permission: Any,
    ): Boolean {
        if (targetType == "project" && permission is ProjectRole) {
            val projectId =
                try {
                    UUID.fromString(targetId.toString())
                } catch (e: IllegalArgumentException) {
                    return false
                }

            val jwt =
                authentication.principal as? org.springframework.security.oauth2.jwt.Jwt
                    ?: return false
            return projectAuthService.hasPermission(projectId, jwt.subject, permission)
        }

        return true
    }
}
