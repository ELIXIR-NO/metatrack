package no.metatrack.server.feature.ontology

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import java.util.UUID

@Entity
data class OntologyTerm(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    val term: String,
    @ManyToOne(fetch = FetchType.LAZY)
    val ontology: Ontology,
)
