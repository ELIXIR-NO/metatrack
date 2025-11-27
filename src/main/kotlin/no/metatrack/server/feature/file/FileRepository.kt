package no.metatrack.server.feature.file

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface FileRepository : JpaRepository<File, UUID>
