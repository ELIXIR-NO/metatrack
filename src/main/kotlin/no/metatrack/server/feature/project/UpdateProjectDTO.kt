package no.metatrack.server.feature.project

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class UpdateProjectDTO(
    @Size(min = 3, max = 32)
    @Pattern(
        regexp = "^[A-Za-z0-9_-]+$",
        message = "Name can only contain letters, digits, hyphens, and underscores (no spaces or special characters).",
    )
    val name: String? = null,
    @Size(max = 256)
    val description: String? = null,
)
