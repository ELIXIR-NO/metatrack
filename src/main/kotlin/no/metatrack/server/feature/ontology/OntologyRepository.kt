package no.metatrack.server.feature.ontology

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OntologyRepository : JpaRepository<Ontology, UUID> {
    fun findAllByProjectId(projectId: UUID): List<Ontology>
}
