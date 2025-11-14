package no.metatrack.server.feature.ontology

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/projects/{projectId}/ontologies")
class OntologyController(
    private val ontologyService: OntologyService,
) {
    @GetMapping
    fun getAllOntologies(
        @PathVariable projectId: UUID,
    ): ResponseEntity<List<OntologyDTO>> {
        val ontologies = ontologyService.getAllOntologySourcesInProject(projectId)
        return ResponseEntity.ok(ontologies.map { OntologyDTO(it) })
    }

    @GetMapping("/{ontologyId}")
    fun getOntologySourceById(
        @PathVariable projectId: UUID,
        @PathVariable ontologyId: UUID,
    ): ResponseEntity<OntologyDTO> {
        val ontology =
            ontologyService.getOntologySourceById(ontologyId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(OntologyDTO(ontology))
    }

    @PostMapping
    fun createNewOntologySet(
        @PathVariable projectId: UUID,
        @Valid @RequestBody request: CreateOntologyDTO,
    ): ResponseEntity<OntologyDTO> {
        val ontology =
            ontologyService.createNewOntologySet(
                projectId,
                request.name,
                request.version,
                request.description,
                request.terms,
            )
        return ResponseEntity.ok(OntologyDTO(ontology))
    }

    @PostMapping("/{ontologyId}/terms")
    fun createNewOntologyTerm(
        @PathVariable ontologyId: UUID,
        @PathVariable projectId: UUID,
        @RequestBody request: CreateOntologyTermDTO,
    ): ResponseEntity<OntologyTermDTO> {
        val ontologyTerm = ontologyService.createOntologyTerm(ontologyId, request.term)
        return ResponseEntity.ok(OntologyTermDTO(ontologyTerm))
    }

    @PutMapping("/{ontologyId}")
    fun updateOntology(
        @PathVariable ontologyId: UUID,
        @PathVariable projectId: UUID,
        @RequestBody request: UpdateOntologyDTO,
    ): ResponseEntity<OntologyDTO> {
        val updateOntology =
            ontologyService.updateOntology(
                ontologyId,
                request.name,
                ontologyVersion = request.version,
                ontologyDescription = request.description,
            )
        return ResponseEntity.ok(OntologyDTO(updateOntology))
    }

    @PutMapping("/{ontologyId}/terms/{termId}")
    fun updateOntologyTerm(
        @PathVariable ontologyId: UUID,
        @PathVariable projectId: UUID,
        @PathVariable termId: UUID,
        @RequestBody request: UpdateOntologyTermDTO,
    ): ResponseEntity<OntologyTermDTO> {
        val ontologyTerm = ontologyService.updateOntologyTerm(termId, request.term)
        return ResponseEntity.ok(OntologyTermDTO(ontologyTerm))
    }

    @DeleteMapping("/{ontologyId}")
    fun deleteOntology(
        @PathVariable ontologyId: UUID,
        @PathVariable projectId: UUID,
    ): ResponseEntity<Unit> {
        ontologyService.deleteOntology(ontologyId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/{ontologyId}/terms/{termId}/")
    fun deleteOntologyTerm(
        @PathVariable ontologyId: UUID,
        @PathVariable projectId: UUID,
        @PathVariable termId: UUID,
    ): ResponseEntity<Unit> {
        ontologyService.deleteOntologyTerm(termId)
        return ResponseEntity.noContent().build()
    }
}
