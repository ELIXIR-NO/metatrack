package no.metatrack.server.feature.project

import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
) {
    fun getAllProjects(): List<Project> = projectRepository.findAll()

    fun getProjectById(id: UUID): Project? = projectRepository.findById(id).orElse(null)

    fun getProjectByName(name: String): Project? = projectRepository.findByName(name).orElse(null)

    fun createNewProject(
        name: String,
        description: String?,
    ): Project =
        projectRepository.save(
            Project(
                name = name,
                description = description,
                createdOn = Instant.now(),
                lastUpdatedOn = Instant.now(),
            ),
        )

    fun updateProject(
        id: UUID,
        name: String?,
        description: String?,
    ): Project {
        val project =
            projectRepository.findById(id).orElseThrow { NoSuchElementException("No project found with id $id") }
        return projectRepository.save(
            project.copy(
                name = name ?: project.name,
                description = description ?: project.description,
                lastUpdatedOn = Instant.now(),
            ),
        )
    }

    fun deleteProject(id: UUID) {
        if (!projectRepository.existsById(id)) {
            throw NoSuchElementException("No project found with id $id")
        }
        projectRepository.deleteById(id)
    }
}
