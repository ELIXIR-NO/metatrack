package no.metatrack.server.feature.project

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.time.Instant
import java.util.UUID

@Entity
data class ProjectMember(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    val project: Project,
    @Column(nullable = false, updatable = false)
    val userId: String,
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val role: ProjectRole,
    val joinedAt: Instant = Instant.now(),
)

enum class ProjectRole {
    OWNER,
    ADMIN,
    EDITOR,
    VIEWER,
}
