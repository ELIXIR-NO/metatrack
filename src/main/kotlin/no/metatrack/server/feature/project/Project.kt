package no.metatrack.server.feature.project

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import no.metatrack.server.feature.ontology.Ontology
import java.time.Instant
import java.util.UUID

@Entity
data class Project(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    @Column(unique = true)
    val name: String,
    val description: String? = null,
    val createdOn: Instant,
    val lastUpdatedOn: Instant,
    @OneToMany(mappedBy = "project")
    val ontologies: MutableSet<Ontology> = mutableSetOf(),
)
