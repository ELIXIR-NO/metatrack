package no.metatrack.server.feature.project

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
@RequestMapping("/api/projects")
class ProjectController(
    private val projectService: ProjectService,
) {
    @GetMapping
    fun getAllProjects(): ResponseEntity<List<ProjectDTO>> {
        val projects = projectService.getAllProjects()
        return ResponseEntity.ok(projects.map { ProjectDTO(it) })
    }

    @GetMapping("/{id}")
    fun getProjectById(
        @PathVariable id: UUID,
    ): ResponseEntity<ProjectDTO> {
        val project = projectService.getProjectById(id) ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(ProjectDTO(project))
    }

    @GetMapping("/name/{name}")
    fun getProjectByName(
        @PathVariable name: String,
    ): ResponseEntity<ProjectDTO> {
        val project = projectService.getProjectByName(name) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(ProjectDTO(project))
    }

    @PostMapping
    fun createNewProject(
        @Valid @RequestBody request: CreateProjectDTO,
    ): ResponseEntity<ProjectDTO> {
        val project = projectService.createNewProject(request.name, request.description)
        return ResponseEntity.ok(ProjectDTO(project))
    }

    @PutMapping("/{id}")
    fun updateProject(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateProjectDTO,
    ): ResponseEntity<ProjectDTO> {
        val project = projectService.updateProject(id, request.name, request.description)
        return ResponseEntity.ok(ProjectDTO(project))
    }

    @DeleteMapping("/{id}")
    fun deleteProject(
        @PathVariable id: UUID,
    ): ResponseEntity<Unit> {
        projectService.deleteProject(id)
        return ResponseEntity.noContent().build()
    }
}
