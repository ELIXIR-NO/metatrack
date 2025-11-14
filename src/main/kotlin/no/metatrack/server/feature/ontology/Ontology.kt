package no.metatrack.server.feature.ontology

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import no.metatrack.server.feature.project.Project
import java.time.Instant
import java.util.UUID

@Entity
data class Ontology(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    val name: String,
    val version: String,
    val description: String? = null,
    val createdOn: Instant,
    val lastUpdatedOn: Instant,
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "ontology", orphanRemoval = true, cascade = [CascadeType.ALL])
    val terms: MutableSet<OntologyTerm> = mutableSetOf(),
    @ManyToOne
    val project: Project,
)
