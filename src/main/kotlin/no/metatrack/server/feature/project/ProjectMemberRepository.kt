package no.metatrack.server.feature.project

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface ProjectMemberRepository : JpaRepository<ProjectMember, UUID> {
    @Query(
        """
        select case when count(pm) > 0 then true else false end
        from ProjectMember pm
        where pm.project.id = :projectId
            and pm.userId = :userId
            and pm.role in :allowedRoles
    """,
    )
    fun hasPermission(
        @Param("projectId") projectId: UUID,
        @Param("userId") userId: String,
        @Param("allowedRoles") allowedRoles: List<ProjectRole>,
    ): Boolean

    @Query("select pm from ProjectMember pm where pm.project.id = :projectId and pm.userId = :userId")
    fun findByProjectIdAndUserId(
        @Param("projectId") projectId: UUID,
        @Param("userId") userId: String,
    ): ProjectMember?

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM ProjectMember pm WHERE pm.project.id = :projectId")
    fun deleteByProjectId(
        @Param("projectId") projectId: UUID,
    )
}
