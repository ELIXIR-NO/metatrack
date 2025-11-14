package no.metatrack.server.feature.sample

import io.minio.GetObjectArgs
import io.minio.MinioClient
import no.metatrack.server.feature.file.CsvFileProcessor
import no.metatrack.server.feature.file.FileService
import no.metatrack.server.feature.project.ProjectRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

@Service
class SampleService(
    private val sampleRepository: SampleRepository,
    private val projectRepository: ProjectRepository,
    private val fileService: FileService,
    private val minioClient: MinioClient,
    private val csvFileProcessor: CsvFileProcessor,
) {
    fun getAllSamplesInProject(projectId: UUID): List<Sample> = sampleRepository.findAllByProjectId(projectId)

    fun getSampleById(id: UUID): Sample? = sampleRepository.findById(id).orElse(null)

    fun getSampleByName(name: String): Sample? = sampleRepository.findByAlias(name).orElse(null)

    @Transactional
    fun createNewSample(
        projectId: UUID,
        alias: String,
        taxId: Int,
        mlst: String?,
        isolationSource: String?,
        collectionDate: LocalDate?,
        geoLocation: String?,
        sequencingLab: String?,
        institution: String?,
        hostHealthState: String?,
    ): Sample {
        val project =
            projectRepository
                .findById(projectId)
                .orElseThrow { NoSuchElementException("No project found with id $projectId") }

        val newSample =
            Sample(
                alias = alias,
                taxId = taxId,
                mlst = mlst,
                isolationSource = isolationSource,
                collectionDate = collectionDate,
                geoLocation = geoLocation,
                sequencingLab = sequencingLab,
                institution = institution,
                hostHealthState = hostHealthState,
                project = project,
                createdOn = Instant.now(),
                lastUpdatedOn = Instant.now(),
            )

        return sampleRepository.save(newSample)
    }

    @Transactional
    fun updateSample(
        sampleId: UUID,
        alias: String?,
        taxId: Int?,
        mlst: String?,
        isolationSource: String?,
        collectionDate: LocalDate?,
        geoLocation: String?,
        sequencingLab: String?,
    ): Sample {
        val sample =
            sampleRepository
                .findById(sampleId)
                .orElseThrow { NoSuchElementException("No sample found with projectId $sampleId") }

        val updatedSample =
            sample.copy(
                alias = alias ?: sample.alias,
                taxId = taxId ?: sample.taxId,
                mlst = mlst ?: sample.mlst,
                isolationSource = isolationSource ?: sample.isolationSource,
                collectionDate = collectionDate ?: sample.collectionDate,
                geoLocation = geoLocation ?: sample.geoLocation,
                sequencingLab = sequencingLab ?: sample.sequencingLab,
                lastUpdatedOn = Instant.now(),
            )

        return sampleRepository.save(updatedSample)
    }

    fun deleteSample(id: UUID) {
        if (!sampleRepository.existsById(id)) {
            throw NoSuchElementException("No sample found with id $id")
        }
        sampleRepository.deleteById(id)
    }

    fun uploadSampleSheet(
        projectId: UUID,
        file: MultipartFile,
    ) {
        val project =
            projectRepository.findById(projectId)

        // TODO(): preserve this in db
        val fileId = UUID.randomUUID().toString()

        fileService.uploadFile(
            project.get().name,
            fileId,
            file.inputStream,
            file.contentType ?: "text/csv",
        )

        val inputStream =
            minioClient.getObject(
                GetObjectArgs
                    .builder()
                    .bucket(project.get().name)
                    .`object`(fileId)
                    .build(),
            )

        csvFileProcessor.ingestCSVSampleSheet(projectId, inputStream)
    }
}
