package no.metatrack.server.feature.file

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant
import java.util.UUID

@Entity
data class File(
    @Id @GeneratedValue(strategy = GenerationType.AUTO) val id: UUID? = null,
    var name: String,
    var location: String,
    val fileType: String? = null,
    val size: Long,
    val uploadTime: Instant,
)
