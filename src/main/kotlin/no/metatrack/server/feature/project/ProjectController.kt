package no.metatrack.server.feature.project

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
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
        authentication: Authentication,
    ): ResponseEntity<ProjectDTO> {
        val jwt =
            authentication.principal as? Jwt
                ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val userId = jwt.subject

        val project = projectService.createNewProject(userId, request.name, request.description)
        return ResponseEntity.ok(ProjectDTO(project))
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id, T(no.metatrack.server.feature.project.ProjectRole).EDITOR)")
    fun updateProject(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateProjectDTO,
    ): ResponseEntity<ProjectDTO> {
        val project = projectService.updateProject(id, request.name, request.description)
        return ResponseEntity.ok(ProjectDTO(project))
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id, T(no.metatrack.server.feature.project.ProjectRole).OWNER)")
    fun deleteProject(
        @PathVariable id: UUID,
    ): ResponseEntity<Unit> {
        projectService.deleteProject(id)
        return ResponseEntity.noContent().build()
    }
}
