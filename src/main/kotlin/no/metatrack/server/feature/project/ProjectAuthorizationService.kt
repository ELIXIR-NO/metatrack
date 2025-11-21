package no.metatrack.server.feature.project

import no.metatrack.server.feature.auth.AuthorizationException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProjectAuthorizationService(
    private val projectMemberRepository: ProjectMemberRepository,
    private val projectRepository: ProjectRepository,
) {
    private val roleHierarchy =
        mapOf(
            ProjectRole.OWNER to listOf(ProjectRole.OWNER, ProjectRole.ADMIN, ProjectRole.EDITOR, ProjectRole.VIEWER),
            ProjectRole.ADMIN to listOf(ProjectRole.ADMIN, ProjectRole.EDITOR, ProjectRole.VIEWER),
            ProjectRole.EDITOR to listOf(ProjectRole.EDITOR, ProjectRole.VIEWER),
            ProjectRole.VIEWER to listOf(ProjectRole.VIEWER),
        )

    fun hasPermission(
        projectId: UUID,
        userId: String,
        requiredRole: ProjectRole,
    ): Boolean = projectMemberRepository.hasPermission(projectId, userId, roleHierarchy[requiredRole]!!)

    fun getUserRole(
        projectId: UUID,
        userId: String,
    ): ProjectRole? {
        val member = projectMemberRepository.findByProjectIdAndUserId(projectId, userId)
        return member?.role
    }

    fun addMember(
        projectId: UUID,
        userId: String,
        role: ProjectRole,
        currentUserId: String,
    ) {
        if (!hasPermission(projectId, currentUserId, ProjectRole.ADMIN)) {
            throw AuthorizationException("Only admins can add members")
        }

        val project =
            projectRepository
                .findById(projectId)
                .orElseThrow { ProjectNotFoundException("Project not found") }

        if (projectMemberRepository.findByProjectIdAndUserId(projectId, userId) != null) {
            throw IllegalArgumentException("User already member of project")
        }

        val member =
            ProjectMember(
                project = project,
                userId = userId,
                role = role,
            )
        projectMemberRepository.save(member)
    }
}
