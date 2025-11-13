package no.metatrack.server.feature.sample

import java.time.Instant
import java.time.LocalDate

data class UpdateSampleDTO(
    val alias: String?,
    val taxId: Int?,
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
