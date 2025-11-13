package no.metatrack.server.feature.sample

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import no.metatrack.server.feature.project.Project
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Entity
data class Sample(
    @Id @GeneratedValue(strategy = GenerationType.UUID) val id: UUID? = null,
    @Column(unique = true) val alias: String,
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
    @ManyToOne(fetch = FetchType.LAZY)
    val project: Project,
)
