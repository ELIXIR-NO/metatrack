package no.metatrack.server.feature.ontology

data class CreateOntologyDTO(
    val name: String,
    val version: String,
    val description: String? = null,
    val terms: List<String> = listOf(),
)
