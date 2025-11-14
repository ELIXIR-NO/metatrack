package no.metatrack.server.feature.ontology

import java.util.UUID

data class OntologyDTO(
    val id: UUID,
    val name: String,
    val version: String,
    val description: String? = null,
    val terms: List<String> = listOf(),
) {
    constructor(ontology: Ontology) : this(
        ontology.id!!,
        ontology.name,
        ontology.version,
        ontology.description,
        ontology.terms.map { it.term },
    )
}
