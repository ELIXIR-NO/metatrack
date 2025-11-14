package no.metatrack.server.feature.ontology

import no.metatrack.server.feature.project.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class OntologyService(
    private val ontologyRepository: OntologyRepository,
    private val ontologyTermRepository: OntologyTermRepository,
    private val projectRepository: ProjectRepository,
) {
    fun getAllOntologySourcesInProject(projectId: UUID): List<Ontology> = ontologyRepository.findAllByProjectId(projectId)

    fun getOntologySourceById(ontologyId: UUID): Ontology? =
        ontologyRepository
            .findById(ontologyId)
            .orElse(null)

    @Transactional
    fun createNewOntologySet(
        projectId: UUID,
        ontologyName: String,
        ontologyVersion: String,
        ontologyDescription: String?,
        ontologyTerms: List<String>,
    ): Ontology {
        val project =
            projectRepository
                .findById(projectId)
                .orElseThrow { NoSuchElementException("No project found with id $projectId") }

        val newOntology =
            Ontology(
                name = ontologyName,
                version = ontologyVersion,
                description = ontologyDescription,
                createdOn = Instant.now(),
                lastUpdatedOn = Instant.now(),
                project = project,
            )

        ontologyTerms.forEach {
            newOntology.terms.add(OntologyTerm(term = it, ontology = newOntology))
        }

        return ontologyRepository.save(newOntology)
    }

    @Transactional
    fun updateOntology(
        ontologyId: UUID,
        ontologyName: String?,
        ontologyVersion: String?,
        ontologyDescription: String?,
    ): Ontology {
        val ontology =
            ontologyRepository
                .findById(ontologyId)
                .orElseThrow { NoSuchElementException("No ontology found with id $ontologyId") }

        return ontologyRepository.save(
            ontology.copy(
                name = ontologyName ?: ontology.name,
                version = ontologyVersion ?: ontology.version,
                description = ontologyDescription ?: ontology.description,
                lastUpdatedOn = Instant.now(),
            ),
        )
    }

    @Transactional
    fun createOntologyTerm(
        ontologyId: UUID,
        term: String,
    ): OntologyTerm {
        val ontology =
            ontologyRepository
                .findById(ontologyId)
                .orElseThrow { NoSuchElementException("No ontology found with id $ontologyId") }
        return ontologyTermRepository.save(OntologyTerm(term = term, ontology = ontology))
    }

    @Transactional
    fun updateOntologyTerm(
        ontologyTermId: UUID,
        term: String,
    ): OntologyTerm =
        ontologyTermRepository.save(
            ontologyTermRepository
                .findById(ontologyTermId)
                .orElseThrow { NoSuchElementException("No ontology term found with id $ontologyTermId") }
                .copy(term = term),
        )

    @Transactional
    fun deleteOntologyTerm(id: UUID) {
        if (!ontologyTermRepository.existsById(id)) {
            throw NoSuchElementException("No ontology term found with id $id")
        }
        ontologyTermRepository.deleteById(id)
    }

    @Transactional
    fun deleteOntology(id: UUID) {
        if (!ontologyRepository.existsById(id)) {
            throw NoSuchElementException("No ontology found with id $id")
        }
        ontologyRepository.deleteById(id)
    }
}
