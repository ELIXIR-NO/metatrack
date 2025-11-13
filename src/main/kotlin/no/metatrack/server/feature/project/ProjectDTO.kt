package no.metatrack.server.feature.project

import java.util.UUID

data class ProjectDTO(
    val id: UUID,
    val name: String,
    val description: String? = null,
) {
    constructor(project: Project) : this(project.id!!, project.name, project.description)
}
