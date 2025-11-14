package no.metatrack.server.feature.ontology

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OntologyTermRepository : JpaRepository<OntologyTerm, UUID>
