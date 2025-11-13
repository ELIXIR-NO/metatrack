package no.metatrack.server.feature.sample

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant
import java.time.LocalDate

data class CreateSampleDTO(
    @NotBlank
    @Size(min = 3, max = 128)
    val alias: String,
    @NotBlank
    val taxId: Int,
    val mlst: String?,
    val isolationSource: String?,
    val collectionDate: LocalDate?,
    val geoLocation: String?,
    val sequencingLab: String?,
    val institution: String?,
    val hostHealthState: String?,
    val createdOn: Instant,
    val lastUpdatedOn: Instant,
)
