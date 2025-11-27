package no.metatrack.server.feature.file

import io.minio.MinioClient
import io.minio.PutObjectArgs
import no.metatrack.server.helper.MinioHelperService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.InputStream
import java.time.Instant
import java.util.UUID

@Service
class FileService(
    private val minioClient: MinioClient,
    private val minioHelperService: MinioHelperService,
    private val fileRepository: FileRepository,
) {
    fun uploadFile(
        bucketName: String,
        objectName: String,
        fileStream: InputStream,
        contentType: String,
    ) {
        minioHelperService.ensureBucketExists(bucketName)

        minioClient.putObject(
            PutObjectArgs
                .builder()
                .bucket(bucketName)
                .`object`(objectName)
                .stream(fileStream, -1, 10485760)
                .contentType(contentType)
                .build(),
        )
    }

    @Transactional
    fun processUploadedFile(
        bucket: String,
        objectSize: Long,
        objectName: String,
    ) {
        val projectId = UUID.fromString(bucket)
        val originalName = extractOriginalFileName(objectName)
        val fileType = extractFileType(objectName)
        val newFile =
            File(
                name = originalName,
                location = "$projectId/$originalName",
                fileType = fileType,
                size = objectSize,
                uploadTime = Instant.now(),
            )
        fileRepository.save(newFile)
    }

    private fun extractOriginalFileName(objectName: String): String = objectName.split("/").last()

    private fun extractFileType(originalName: String): String? =
        originalName
            .substringAfterLast('.', "")
            .takeIf { it.isNotEmpty() }
}
