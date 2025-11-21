package no.metatrack.server.feature.project

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

@Service
class ProjectService(
    private val projectRepository: ProjectRepository,
    private val projectMemberRepository: ProjectMemberRepository,
) {
    fun getAllProjects(): List<Project> = projectRepository.findAll()

    fun getProjectById(id: UUID): Project? = projectRepository.findById(id).orElse(null)

    fun getProjectByName(name: String): Project? = projectRepository.findByName(name).orElse(null)

    @Transactional
    fun createNewProject(
        userId: String,
        name: String,
        description: String?,
    ): Project {
        val project =
            projectRepository.save(
                Project(
                    name = name,
                    description = description,
                    createdOn = Instant.now(),
                    lastUpdatedOn = Instant.now(),
                ),
            )

        projectMemberRepository.save(
            ProjectMember(
                userId = userId,
                role = ProjectRole.OWNER,
                project = project,
            ),
        )

        return project
    }

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

    @Transactional
    fun deleteProject(id: UUID) {
        if (!projectRepository.existsById(id)) {
            throw NoSuchElementException("No project found with id $id")
        }
        projectMemberRepository.deleteByProjectId(id)
        projectRepository.deleteById(id)
    }
}
