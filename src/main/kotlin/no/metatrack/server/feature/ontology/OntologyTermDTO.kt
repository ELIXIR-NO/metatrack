package no.metatrack.server.feature.ontology

import java.util.UUID

data class OntologyTermDTO(
    val id: UUID,
    val term: String,
) {
    constructor(ontologyTerm: OntologyTerm) : this(
        ontologyTerm.id!!,
        term = ontologyTerm.term,
    )
}
