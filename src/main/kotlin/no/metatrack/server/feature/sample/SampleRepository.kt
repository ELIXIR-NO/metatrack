package no.metatrack.server.feature.sample

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface SampleRepository : JpaRepository<Sample, UUID> {
    fun findAllByProjectId(projectId: UUID): List<Sample>

    fun findByAlias(name: String): Optional<Sample>
}
