package no.metatrack.server.feature.sample

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
@RequestMapping("/api/projects/{projectId}/samples")
class SampleController(
    private val sampleService: SampleService,
) {
    @GetMapping
    fun getAllSamplesInProject(
        @PathVariable projectId: UUID,
    ): ResponseEntity<List<SampleDTO>> {
        val samples = sampleService.getAllSamplesInProject(projectId)
        return ResponseEntity.ok(samples.map { SampleDTO(it) })
    }

    @GetMapping("/{sampleId}")
    fun getSampleById(
        @PathVariable sampleId: UUID,
        @PathVariable projectId: String,
    ): ResponseEntity<SampleDTO> {
        val sample = sampleService.getSampleById(sampleId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SampleDTO(sample))
    }

    @GetMapping("/name/{sampleName}")
    fun getSampleByName(
        @PathVariable sampleName: String,
        @PathVariable projectId: String,
    ): ResponseEntity<SampleDTO> {
        val sample = sampleService.getSampleByName(sampleName) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(SampleDTO(sample))
    }

    @PostMapping
    fun createNewSample(
        @PathVariable projectId: UUID,
        @Valid @RequestBody request: CreateSampleDTO,
    ): ResponseEntity<SampleDTO> =
        ResponseEntity.ok(
            SampleDTO(
                sampleService.createNewSample(
                    projectId,
                    request.alias,
                    request.taxId,
                    request.mlst,
                    request.isolationSource,
                    request.collectionDate,
                    request.geoLocation,
                    request.sequencingLab,
                    request.institution,
                    request.hostHealthState,
                ),
            ),
        )

    @PutMapping("/{sampleId}")
    fun updateSample(
        @PathVariable sampleId: UUID,
        @Valid @RequestBody request: UpdateSampleDTO,
        @PathVariable projectId: String,
    ): ResponseEntity<SampleDTO> =
        ResponseEntity.ok(
            SampleDTO(
                sampleService.updateSample(
                    sampleId,
                    request.alias,
                    request.taxId,
                    request.mlst,
                    request.isolationSource,
                    request.collectionDate,
                    request.geoLocation,
                    request.sequencingLab,
                ),
            ),
        )

    @DeleteMapping("/{sampleId}")
    fun deleteSample(
        @PathVariable sampleId: UUID,
        @PathVariable projectId: String,
    ): ResponseEntity<Unit> {
        sampleService.deleteSample(sampleId)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/upload-sample-sheet")
    fun uploadSampleSheet(
        @RequestParam("file") file: MultipartFile,
        @PathVariable projectId: UUID,
    ): ResponseEntity<Unit> {
        sampleService.uploadSampleSheet(projectId, file)
        return ResponseEntity.noContent().build()
    }
}
