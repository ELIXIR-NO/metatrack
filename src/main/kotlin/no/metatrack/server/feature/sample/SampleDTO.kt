package no.metatrack.server.feature.sample

import java.time.Instant
import java.time.LocalDate
import java.util.UUID

data class SampleDTO(
    val id: UUID,
    val alias: String,
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
) {
    constructor(sample: Sample) : this(
        sample.id!!,
        sample.alias,
        sample.taxId,
        sample.mlst,
        sample.isolationSource,
        sample.collectionDate,
        sample.geoLocation,
        sample.sequencingLab,
        sample.institution,
        sample.hostHealthState,
        sample.createdOn,
        sample.lastUpdatedOn,
    )
}
