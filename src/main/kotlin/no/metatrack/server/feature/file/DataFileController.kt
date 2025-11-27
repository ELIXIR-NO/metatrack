package no.metatrack.server.feature.file

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import no.metatrack.server.helper.MinioHelperService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/projects/{projectId}/files/presigned-url")
class DataFileController(
    private val minioHelperService: MinioHelperService,
) {
    @GetMapping("/get")
    @PreAuthorize("hasPermission(#projectId, T(no.metatrack.server.feature.project.ProjectRole).VIEWER)")
    fun getPresignedGetUrl(
        @PathVariable("projectId") projectId: UUID,
        @Valid @RequestBody request: FileDataRequestDTO,
    ): ResponseEntity<PreSignedUrlResponseDTO> {
        val url = minioHelperService.generatePreSignedGetUrl(projectId, request.fileName, request.expiryMinutes)
        return ResponseEntity.ok(PreSignedUrlResponseDTO(url))
    }

    @PutMapping("/put")
    fun getPresignedPutUrl(
        @PathVariable("projectId") projectId: UUID,
        @Valid @RequestBody request: FileDataRequestDTO,
    ): ResponseEntity<PreSignedUrlResponseDTO> {
        val url = minioHelperService.generatePreSignedPutUrl(projectId, request.fileName, request.expiryMinutes)
        return ResponseEntity.ok(PreSignedUrlResponseDTO(url))
    }
}

data class FileDataRequestDTO(
    @NotBlank
    val fileName: String,
    val expiryMinutes: Int = 60,
)

data class PreSignedUrlResponseDTO(
    val url: String,
)
